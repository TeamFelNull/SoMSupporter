package dev.felnull.somsupporter.listener;

import dev.felnull.somsupporter.Notifier;
import dev.felnull.somsupporter.Somsupporter;
import dev.felnull.somsupporter.sound.SomsoundEvents;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    private static final List<String> notifierBlackList;
    private static final List<String> specialItemList;
    static{
        notifierBlackList = Arrays.asList(
                "ブラックストーン",
                "砂の記憶",
                "星の欠片",
                "星"
        );
        specialItemList = Arrays.asList(
                "異物混入―紅―",
                "異物混入―蒼―"
        );
    }

    static class Hit {
        final long tMs;
        final double dmg;
        Hit(long tMs, double dmg){ this.tMs=tMs; this.dmg=dmg; }
    }

    // === 設定値（必要なら後でconfig化） ===
    private static final double WINDOW_SEC = 10.0;               // 直近DPSの窓
    private static final Pattern PAT_ARROW_NUM = Pattern.compile("▶\\s*([0-9]+(?:\\.[0-9]+)?)");
    private static final String[] IGNORE_PREFIXES = { "+[", "EXP" }; // ノイズ行を除外

    private static final Pattern PCT_IN_BRACKETS = Pattern.compile("\\[(\\d+(?:\\.\\d+)?)%\\]");

    // === 状態 ===
    private static final Deque<Hit> ring = new ArrayDeque<>();
    private static double sessionTotal = 0.0;
    private static Long sessionStartMs = null;
    private static long lastSoundTime = 0L;
    private static final long SOUND_COOLDOWN_MS = 3000;

    // === 入口: チャット受信 ===
    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent e) {
        String raw = e.getMessage().getString();
        String s = stripFormatting(raw);
        s = s.trim();
        if (s.isEmpty()) return;

        // --- レアドロップ通知 ---
        if (s.startsWith("+[")) {
            // 角括弧内のパーセンテージを全て調べる
            Matcher pm = PCT_IN_BRACKETS.matcher(s);
            double min = Double.POSITIVE_INFINITY;
            boolean any = false;
            while (pm.find()) {
                any = true;
                try {
                    double v = Double.parseDouble(pm.group(1));
                    min = Math.min(min, v);
                } catch (NumberFormatException ignored) {}
            }
            // 何かしら%が見つかって、最小値が1未満なら通知＆音
            // 右下トースト：アイテム名だけ抽出（最初の +[ ... ] 部分をそのまま使う）
            int endIdx = s.indexOf(']') ;
            String title = (endIdx > 1) ? s.substring("+[".length(), endIdx) : s;
            if ((any && min <= 5.0) || specialItemList.contains(title)) {
                if(notifierBlackList.contains(title)){ //ブラックリストアイテム除外
                    return;
                }

                Notifier.push(String.format("%s 低確率: %.2f%%", title, min));

                // 効果音（UIトースト音など好みで）
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player != null) {
                    long now = System.currentTimeMillis();
                    if (now - lastSoundTime > SOUND_COOLDOWN_MS) {
                        mc.player.playSound(SomsoundEvents.NOTIFY_LOWCHANCE.get(), 1.0F, 1.0F);
                        lastSoundTime = now;
                    }
                }
            }
            return; // 取得行はDPS集計に関係ないのでここで終わり
        }
        // --- DPS用処理 ---
        for (String pref : IGNORE_PREFIXES) {
            if (s.startsWith(pref)) return;
        }

        Matcher m = PAT_ARROW_NUM.matcher(s);
        if (!m.find()) return;

        double val;
        try { val = Double.parseDouble(m.group(1)); }
        catch (NumberFormatException ex) { return; }

        long now = System.currentTimeMillis();
        ring.addLast(new Hit(now, val));
        sessionTotal += val;
        if (sessionStartMs == null) sessionStartMs = now;
        prune(now);
    }

    private static String stripFormatting(String text) {
        if (text == null) return null;
        return text.replaceAll("§.", "");
    }
    // === 内部: 古いヒットを窓から除去 ===
    private static void prune(long nowMs) {
        long keep = (long)(WINDOW_SEC * 1000.0);
        while (!ring.isEmpty() && nowMs - ring.peekFirst().tMs > keep) {
            ring.removeFirst();
        }

        if (ring.isEmpty() && sessionStartMs != null && nowMs - sessionStartMs > 30000) {
            resetSession();
        }
    }

    // === 内部: 窓の経過秒 ===
    private static double spanSec(long nowMs) {
        if (ring.isEmpty()) return 0.0;
        return (nowMs - ring.peekFirst().tMs) / 1000.0;
    }

    // === HUDから参照されるゲッター ===
    public static double getWindowDps() {
        long now = System.currentTimeMillis();
        prune(now);
        double sum = ring.stream().mapToDouble(h -> h.dmg).sum();
        double span = Math.min(WINDOW_SEC, spanSec(now));
        return span > 0 ? (sum / span) : 0.0;
    }

    public static double getSessionDps() {
        if (sessionStartMs == null) return 0.0;
        long now = System.currentTimeMillis();
        double sec = (now - sessionStartMs)/1000.0;
        return sec > 0 ? (sessionTotal / sec) : 0.0;
    }

    public static double getSessionTotal() {
        return sessionTotal;
    }

    public static void resetSession() {
        ring.clear();
        sessionTotal = 0.0;
        sessionStartMs = null;
    }
}