package dev.felnull.somsupporter.listener;

import dev.felnull.somsupporter.Notifier;
import dev.felnull.somsupporter.sound.SomsoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    private static final List<String> notifierBlackList;
    private static final List<String> specialItemList;

    static {
        notifierBlackList = Arrays.asList(
                "ブラックストーン",
                "砂の記憶",
                "星の欠片",
                "星",
                "エテナの欠片"
        );
        specialItemList = Arrays.asList(
                "異物混入―紅―",
                "異物混入―蒼―"
        );
    }

    static class Hit {
        final long tMs;
        final double dmg;
        Hit(long tMs, double dmg) { this.tMs = tMs; this.dmg = dmg; }
    }

    // === 設定値 ===
    private static final double WINDOW_SEC = 10.0;
    private static final Pattern PAT_ARROW_NUM = Pattern.compile("▶\\s*([0-9]+(?:\\.[0-9]+)?)");
    private static final String[] IGNORE_PREFIXES = { "+[", "EXP" };

    private static final Pattern PCT_IN_BRACKETS = Pattern.compile("\\[(\\d+(?:\\.\\d+)?)%\\]");
    private static final Pattern PAT_DAMAGE_LOG = Pattern.compile("◀\\s*([0-9]+(?:\\.[0-9]+)?)\\s*\\[([^\\]]+)\\]");

    // === 状態 ===
    private static final Deque<Hit> ring = new ArrayDeque<>();
    private static double sessionTotal = 0.0;
    private static Long sessionStartMs = null;
    private static long lastSoundTime = 0L;
    private static final long SOUND_COOLDOWN_MS = 3000;

    private static long loginTimeMs = 0L;
    private static final long DANGER_MS = 10_000L;

    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            onChatMessage(message.getString());
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            loginTimeMs = System.currentTimeMillis();
        });
    }

    private static void onChatMessage(String raw) {
        String s = stripFormatting(raw);
        if (s == null) return;
        s = s.trim();
        if (s.isEmpty()) return;

        // --- 被ダメージ音再生処理 ---
        Matcher dm = PAT_DAMAGE_LOG.matcher(s);
        if (dm.find()) {
            String detail = dm.group(2).toUpperCase();
            String type = detail.endsWith("%") ? "PERCENT" : detail;

            switch (type) {
                case "PERCENT" -> playDmgSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F);
                case "FALL"    -> playDmgSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F);
                case "LAVA"    -> playDmgSound(SoundEvents.PLAYER_HURT_ON_FIRE, 1.0F, 1.2F);
                case "DROWNING"-> playDmgSound(SoundEvents.PLAYER_HURT_DROWN, 1.0F, 1.0F);
                default        -> playDmgSound(SoundEvents.PLAYER_HURT, 1.0F, 0.8F);
            }
        }

        // --- レアドロップ通知 ---
        if (s.startsWith("+[")) {
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

            int endIdx = s.indexOf(']');
            String title = (endIdx > 1) ? s.substring("+[".length(), endIdx) : s;
            if ((any && min <= 5.0) || specialItemList.contains(title)) {
                if (notifierBlackList.contains(title)) {
                    return;
                }

                Notifier.push(String.format("%s 低確率: %.2f%%", title, min));

                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    long now = System.currentTimeMillis();
                    if (now - lastSoundTime > SOUND_COOLDOWN_MS) {
                        mc.getSoundManager().play(SimpleSoundInstance.forUI(SomsoundEvents.NOTIFY_LOWCHANCE, 1.0F, 1.0F));
                        lastSoundTime = now;
                    }
                }
            }
            return;
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

    private static void prune(long nowMs) {
        long keep = (long)(WINDOW_SEC * 1000.0);
        while (!ring.isEmpty() && nowMs - ring.peekFirst().tMs > keep) {
            ring.removeFirst();
        }

        if (ring.isEmpty() && sessionStartMs != null && nowMs - sessionStartMs > 30000) {
            resetSession();
        }
    }

    private static double spanSec(long nowMs) {
        if (ring.isEmpty()) return 0.0;
        return (nowMs - ring.peekFirst().tMs) / 1000.0;
    }

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
        double sec = (now - sessionStartMs) / 1000.0;
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

    public static boolean shouldWarnOnLogout() {
        if (loginTimeMs == 0L) return false;
        long elapsed = System.currentTimeMillis() - loginTimeMs;
        return elapsed <= DANGER_MS;
    }

    private static void playDmgSound(SoundEvent sound, float volume, float pitch) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && sound != null) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
        }
    }
}
