package dev.felnull.somsupporter.mixin;

import dev.felnull.somsupporter.renderer.ItemOverlayRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;", ordinal = 0))
    private void renderGuiItemDecorationsInject(FontRenderer p_180453_1_, ItemStack stack, int x, int y, String p_180453_5_, CallbackInfo ci) {
        ItemOverlayRenderer.render(stack, x, y);
    }
}
