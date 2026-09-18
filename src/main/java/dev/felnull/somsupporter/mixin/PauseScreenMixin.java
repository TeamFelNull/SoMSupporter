package dev.felnull.somsupporter.mixin;

import dev.felnull.somsupporter.gui.ConfirmEarlyLogoutScreen;
import dev.felnull.somsupporter.listener.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void somsupporter$afterInit(CallbackInfo ci) {
        if (!ClientEvents.shouldWarnOnLogout()) return;

        String disconnectText = I18n.get("menu.disconnect");
        Screen self = (Screen)(Object)this;
        ScreenAccessor accessor = (ScreenAccessor)(Object)this;

        List<Renderable> renderables = accessor.getRenderables();
        List<GuiEventListener> children = accessor.getChildren();

        Button toReplace = null;
        for (Renderable r : renderables) {
            if (r instanceof Button button && disconnectText.equals(button.getMessage().getString())) {
                toReplace = button;
                break;
            }
        }

        if (toReplace == null) return;

        Button old = toReplace;
        Button wrapped = Button.builder(old.getMessage(), b ->
            Minecraft.getInstance().setScreen(new ConfirmEarlyLogoutScreen(self))
        ).bounds(old.getX(), old.getY(), old.getWidth(), old.getHeight()).build();

        int ri = renderables.indexOf(old);
        if (ri >= 0) renderables.set(ri, wrapped);

        int childIdx = children.indexOf(old);
        if (childIdx >= 0) children.set(childIdx, wrapped);
    }
}
