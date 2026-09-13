package dev.felnull.somsupporter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HudPanelRenderer {

    private static int panelX = 8;
    private static int panelY = 28;
    private static int panelW = 170;
    private static int panelH = 56;

    private static boolean visible = true;

    private static final int BG_COLOR          = 0x33000000;
    private static final int MAIN_BORDER_COLOR = 0x11FFFFFF;
    private static final int TITLE_COLOR       = 0xFF66CCFF;
    private static final int TEXT_COLOR        = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onRender(RenderGuiOverlayEvent.Post e) {
        if (!visible) return;

        // 描画タイミングの判定（旧 RenderGameOverlayEvent.ElementType.ALL 相当）
        // 画面全体のオーバーレイ描画が終わるタイミング（HOTBAR など）を判定
        if (!e.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) return;

        Minecraft mc = Minecraft.getInstance();
        Font fr = mc.font;

        // 1.20.4 では描画オブジェクトとして GuiGraphics を使用
        GuiGraphics guiGraphics = e.getGuiGraphics();

        // 画面サイズ取得
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        int x = Math.max(0, Math.min(panelX, sw - panelW));
        int y = Math.max(0, Math.min(panelY, sh - panelH));

        // GuiGraphics#fill を直接呼び出し
        guiGraphics.fill(x, y, x + panelW, y + panelH, BG_COLOR);
        drawBorder(guiGraphics, x, y, panelW, panelH, MAIN_BORDER_COLOR);

        int tx = x + 6;
        int ty = y + 6;

        // 文字描画: fr.draw(...) -> guiGraphics.drawString(font, text, x, y, color, dropShadow)
        guiGraphics.drawString(fr, "DPS Checker", tx, ty, TITLE_COLOR, false);
        ty += 12;

        double dpsW  = DpsNumbers.getWindowDps();
        double dpsS  = DpsNumbers.getSessionDps();
        double total = DpsNumbers.getSessionTotal();

        guiGraphics.drawString(fr, String.format("10秒平均:  %.1f DPS", dpsW), tx, ty, TEXT_COLOR, false); ty += 10;
        guiGraphics.drawString(fr, String.format("攻撃中平均: %.1f DPS", dpsS), tx, ty, TEXT_COLOR, false); ty += 10;
        guiGraphics.drawString(fr, String.format("Total:   %.0f", total),   tx, ty, TEXT_COLOR, false);

        // Notifier 側の引数も GuiGraphics に合わせて更新してください
        Notifier.render(guiGraphics);
    }

    private static void drawBorder(GuiGraphics guiGraphics, int x, int y, int w, int h, int color) {
        guiGraphics.fill(x, y, x + w, y + 1, color);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, color);
        guiGraphics.fill(x, y, x + 1, y + h, color);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, color);
    }
}