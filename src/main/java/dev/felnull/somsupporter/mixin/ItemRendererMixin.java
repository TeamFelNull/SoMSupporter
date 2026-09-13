package dev.felnull.somsupporter.mixin;

import dev.felnull.somsupporter.renderer.ItemOverlayRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(GuiGraphics.class)
public class ItemRendererMixin {

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL")
    )
    private void renderGuiItemDecorationsInject(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (stack != null && !stack.isEmpty()) {
            ItemOverlayRenderer.render((GuiGraphics) (Object) this, stack, x, y);
        }
    }
}