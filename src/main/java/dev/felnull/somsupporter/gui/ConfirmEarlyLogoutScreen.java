package dev.felnull.somsupporter.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfirmEarlyLogoutScreen extends Screen {
    private final Screen parent;

    public ConfirmEarlyLogoutScreen(Screen parent) {
        super(new StringTextComponent("ログアウト確認"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addButton(new Button(
                centerX - 105, centerY + 10,
                100, 20,
                new StringTextComponent("キャンセル"),
                b -> Minecraft.getInstance().setScreen(parent)
        ));

        this.addButton(new Button(
                centerX + 5, centerY + 10,
                100, 20,
                new StringTextComponent("OK"),
                b -> doLogout()
        ));
    }

    @Override
    public void render(com.mojang.blaze3d.matrix.MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font,
                "本当にログアウトしますか？", this.width / 2, this.height / 2 - 30, 0xFFFFFF);
        drawCenteredString(matrixStack, this.font,
                "データが消失する可能性があります！", this.width / 2, this.height / 2 - 18, 0xFF5555);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void doLogout() {
        Minecraft mc = Minecraft.getInstance();
        boolean integrated = mc.isLocalServer();

        if (mc.level != null) {
            mc.level.disconnect();
        }

        if (integrated) {
            mc.clearLevel(new DirtMessageScreen(
                    new TranslationTextComponent("menu.savingLevel")));
        } else {
            mc.clearLevel();
        }

        mc.setScreen(new MainMenuScreen());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}