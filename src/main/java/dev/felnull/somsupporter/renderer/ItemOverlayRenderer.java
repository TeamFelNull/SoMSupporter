package dev.felnull.somsupporter.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.felnull.somsupporter.util.SomUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemOverlayRenderer {

    public static void render(ItemStack stack, int x, int y) {
        renderSomDurabilityBar(stack, x, y);
    }

    private static void renderSomDurabilityBar(ItemStack stack, int x, int y) {
        List<String> loreTextList = SomUtils.getLore(stack);
        Pair<Integer, Integer> durability = SomUtils.getDurability(loreTextList);

        if (durability != null && durability.getLeft() < durability.getRight()) {
            // RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            float health = 1f - ((float) durability.getLeft()) / ((float) durability.getRight());
            int len = Math.round(13.0f - health * 13.0f);

            float h = (MathHelper.lerp(health, 345f, 365f) / 360f) % 1f;
            float s = MathHelper.lerp(health, 71f, 40f) / 100f;
            float v = MathHelper.lerp(health, 87f, 99f) / 100f;
            int color = MathHelper.hsvToRgb(h, s, v);

            double yOffset = stack.getItem().showDurabilityBar(stack) ? 2.5 : 0;

            fillRect(bufferbuilder, x + 2, (double) (y + 13) - yOffset, 13, 2, 0, 0, 0, 255);
            fillRect(bufferbuilder, x + 2, (double) (y + 13) - yOffset, len, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);

            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
            //  RenderSystem.enableDepthTest();
        }
    }

    private static void fillRect(BufferBuilder bufferBuilder, int x, double y, int width, int height, int r, int g, int b, int a) {
        float z = 400;
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(x, y, z).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x, y + height, z).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y + height, z).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y, z).color(r, g, b, a).endVertex();
        Tessellator.getInstance().end();
    }
}
