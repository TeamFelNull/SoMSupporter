package dev.felnull.somsupporter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notifier extends AbstractGui {
    private static class Toast {
        final String text;
        final long untilMs;
        Toast(String t, long u){ this.text = t; this.untilMs = u; }
    }
    private static final List<Toast> TOASTS = new ArrayList<>();
    private static final int DURATION_MS = 10000; // 3秒表示
    private static final int BG = 0x88000000;    // 半透明黒
    private static final int BORDER = 0xFFFFFFFF;
    private static final int TEXT = 0xFFFFFFFF;

    public static void push(String text){
        TOASTS.add(new Toast(text, System.currentTimeMillis() + DURATION_MS));
    }

    // HUD側の描画イベントから呼び出す
    public static void render(MatrixStack ms){
        if (TOASTS.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        long now = System.currentTimeMillis();
        // 期限切れを掃除
        for (Iterator<Toast> it = TOASTS.iterator(); it.hasNext(); ) {
            if (now > it.next().untilMs) it.remove();
        }
        if (TOASTS.isEmpty()) return;

        int margin = 8, pad = 6, gap = 4;
        int y = sh - margin; // 右下から上に積む

        // 下にあるものほど新しいように積む
        for (int i = TOASTS.size()-1; i >= 0; i--) {
            String txt = TOASTS.get(i).text;
            int w = fr.width(txt) + pad*2;
            int h = 14;
            int x = sw - margin - w;
            y -= h;

            fill(ms, x, y, x+w, y+h, BG);
            // 上側に細い白線で見切りをつける
            fill(ms, x, y, x+w, y+1, BORDER);
            fr.draw(ms, txt, x + pad, y + 4, TEXT);

            y -= gap;
        }
    }
}