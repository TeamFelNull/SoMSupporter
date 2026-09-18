package dev.felnull.somsupporter.renderer;

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

            float h = (Mth.lerp(health, 345f, 365f) / 360f) % 1f;
            float s = Mth.lerp(health, 71f, 40f) / 100f;
            float v = Mth.lerp(health, 87f, 99f) / 100f;

            int rgb = Mth.hsvToRgb(h, s, v);
            int color = 0xFF000000 | rgb;

            double yOffset = stack.isBarVisible() ? 2.5 : 0;

            int barX = x + 2;
            int barY = (int) ((y + 13) - yOffset);

            guiGraphics.pose().pushPose();
            // レイヤー200
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);

            // 下地（黒背景）
            guiGraphics.fill(barX, barY, barX + 13, barY + 2, 0xFF000000);
            // バー（カラー）
            guiGraphics.fill(barX, barY, barX + len, barY + 1, color);

            guiGraphics.pose().popPose();
        }
    }
}
