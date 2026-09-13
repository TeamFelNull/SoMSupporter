package dev.felnull.somsupporter.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.felnull.somsupporter.util.SomUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemOverlayRenderer {

    // GuiGraphics を受け取る形に修正
    public static void render(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        renderSomDurabilityBar(guiGraphics, stack, x, y);
    }

    private static void renderSomDurabilityBar(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        List<String> loreTextList = SomUtils.getLore(stack);
        Pair<Integer, Integer> durability = SomUtils.getDurability(loreTextList);

        if (durability != null && durability.getLeft() < durability.getRight()) {
            float health = 1f - ((float) durability.getLeft()) / ((float) durability.getRight());
            int len = Math.round(13.0f - health * 13.0f);

            // MathHelper -> Mth に変更
            float h = (Mth.lerp(health, 345f, 365f) / 360f) % 1f;
            float s = Mth.lerp(health, 71f, 40f) / 100f;
            float v = Mth.lerp(health, 87f, 99f) / 100f;

            // HSV -> RGB (ARGB形式の int を取得)
            int rgb = Mth.hsvToRgb(h, s, v);
            int color = 0xFF000000 | rgb; // 不透明度 (Alpha = 255) を付与

            // Vanillaの耐久値バーが表示されている場合のオフセット
            // 1.20.4 では stack.isBarVisible() または ItemExtension を利用
            double yOffset = stack.isBarVisible() ? 2.5 : 0;

            int barX = x + 2;
            int barY = (int) ((y + 13) - yOffset);

            // GuiGraphics を使用した矩形描画 (z座標や描画設定のON/OFFは GuiGraphics が自動処理)
            // 下地（黒背景）
            guiGraphics.fill(barX, barY, barX + 13, barY + 2, 0xFF000000);
            // バー（カラー）
            guiGraphics.fill(barX, barY, barX + len, barY + 1, color);
        }
    }
}
