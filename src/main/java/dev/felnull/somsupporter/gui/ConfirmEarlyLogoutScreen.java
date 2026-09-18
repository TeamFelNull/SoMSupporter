package dev.felnull.somsupporter.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ConfirmEarlyLogoutScreen extends Screen {
    private final Screen parent;

    public ConfirmEarlyLogoutScreen(Screen parent) {
        super(Component.literal("ログアウト確認"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(
                Button.builder(Component.literal("キャンセル"), b -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreen(parent);
                            }
                        })
                        .bounds(centerX - 105, centerY + 10, 100, 20)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("OK"), b -> doLogout())
                        .bounds(centerX + 5, centerY + 10, 100, 20)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(
                this.font,
                "本当にログアウトしますか？",
                this.width / 2,
                this.height / 2 - 30,
                0xFFFFFF
        );

        guiGraphics.drawCenteredString(
                this.font,
                "データが消失する可能性があります！",
                this.width / 2,
                this.height / 2 - 18,
                0xFF5555
        );

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void doLogout() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null) {
            mc.disconnect(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        }

        mc.setScreen(new TitleScreen());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
