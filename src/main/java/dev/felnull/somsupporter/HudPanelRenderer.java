package dev.felnull.somsupporter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public class HudPanelRenderer {

    private static final int panelX = 8;
    private static final int panelY = 28;
    private static final int panelW = 170;
    private static final int panelH = 56;

    private static boolean visible = true;

    private static final int BG_COLOR          = 0x33000000;
    private static final int MAIN_BORDER_COLOR = 0x11FFFFFF;
    private static final int TITLE_COLOR       = 0xFF66CCFF;
    private static final int TEXT_COLOR        = 0xFFFFFFFF;

    public static void register() {
        HudRenderCallback.EVENT.register(HudPanelRenderer::onRender);
    }

    private static void onRender(GuiGraphics guiGraphics, float tickDelta) {
        if (!visible) return;

        Minecraft mc = Minecraft.getInstance();
        Font fr = mc.font;

        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        int x = Math.max(0, Math.min(panelX, sw - panelW));
        int y = Math.max(0, Math.min(panelY, sh - panelH));

        guiGraphics.fill(x, y, x + panelW, y + panelH, BG_COLOR);
        drawBorder(guiGraphics, x, y, panelW, panelH, MAIN_BORDER_COLOR);

        int tx = x + 6;
        int ty = y + 6;

        guiGraphics.drawString(fr, "DPS Checker", tx, ty, TITLE_COLOR, false);
        ty += 12;

        double dpsW  = DpsNumbers.getWindowDps();
        double dpsS  = DpsNumbers.getSessionDps();
        double total = DpsNumbers.getSessionTotal();

        guiGraphics.drawString(fr, String.format("10秒平均:  %.1f DPS", dpsW), tx, ty, TEXT_COLOR, false); ty += 10;
        guiGraphics.drawString(fr, String.format("攻撃中平均: %.1f DPS", dpsS), tx, ty, TEXT_COLOR, false); ty += 10;
        guiGraphics.drawString(fr, String.format("Total:   %.0f", total),   tx, ty, TEXT_COLOR, false);

        Notifier.render(guiGraphics);
    }

    private static void drawBorder(GuiGraphics guiGraphics, int x, int y, int w, int h, int color) {
        guiGraphics.fill(x, y, x + w, y + 1, color);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, color);
        guiGraphics.fill(x, y, x + 1, y + h, color);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, color);
    }
}
