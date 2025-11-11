package dev.felnull.somsupporter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HudPanelRenderer extends AbstractGui {

    private static int panelX = 8;
    private static int panelY = 28;
    private static int panelW = 170;
    private static int panelH = 56;

    private static boolean visible = true;

    private static final int BG_COLOR     = 0x33000000;
    private static final int MAIN_BORDER_COLOR = 0x11FFFFFF;
    private static final int TITLE_COLOR  = 0xFF66CCFF;
    private static final int TEXT_COLOR   = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post e) {
        if (!visible) return;
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;

        MatrixStack ms = e.getMatrixStack();

        // 画面サイズ取得
        int sw = mc.getWindow().getGuiScaledWidth();   // getMainWindow() → getWindow()
        int sh = mc.getWindow().getGuiScaledHeight();

        int x = Math.max(0, Math.min(panelX, sw - panelW));
        int y = Math.max(0, Math.min(panelY, sh - panelH));

        fill(ms, x, y, x + panelW, y + panelH, BG_COLOR);
        drawBorder(ms, x, y, panelW, panelH, MAIN_BORDER_COLOR);

        int tx = x + 6;
        int ty = y + 6;

        fr.draw(ms, "DPS Checker", tx, ty, TITLE_COLOR);
        ty += 12;
        double dpsW  = DpsNumbers.getWindowDps();
        double dpsS  = DpsNumbers.getSessionDps();
        double total = DpsNumbers.getSessionTotal();

        fr.draw(ms, String.format("10秒平均:  %.1f DPS", dpsW), tx, ty, TEXT_COLOR); ty += 10;
        fr.draw(ms, String.format("攻撃中平均: %.1f DPS", dpsS), tx, ty, TEXT_COLOR); ty += 10;
        fr.draw(ms, String.format("Total:   %.0f", total),   tx, ty, TEXT_COLOR);

        Notifier.render(e.getMatrixStack());
    }

    private static void drawBorder(MatrixStack ms, int x, int y, int w, int h, int color) {
        fill(ms, x, y, x + w, y + 1, color);
        fill(ms, x, y + h - 1, x + w, y + h, color);
        fill(ms, x, y, x + 1, y + h, color);
        fill(ms, x + w - 1, y, x + w, y + h, color);
    }
    /**
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e) {
        final int PRESS = 1;
        if (e.getAction() != PRESS) return;

        final int KEY_LEFT  = 263, KEY_RIGHT = 262, KEY_UP = 265, KEY_DOWN = 264;
        final int KEY_H = 72, KEY_R = 82;

        if (e.getKey() == KEY_H) {
            visible = !visible;
        } else if (e.getKey() == KEY_R) {
            DpsNumbers.resetSession();
        } else if (e.getKey() == KEY_LEFT) {
            panelX -= 2;
        } else if (e.getKey() == KEY_RIGHT) {
            panelX += 2;
        } else if (e.getKey() == KEY_UP) {
            panelY -= 2;
        } else if (e.getKey() == KEY_DOWN) {
            panelY += 2;
        }
    }
    **/
}