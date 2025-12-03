package dev.felnull.somsupporter.gui;

import dev.felnull.somsupporter.feature.party.PartyActions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;

public class PartyMainScreen extends Screen {

    public PartyMainScreen() {
        super(new StringTextComponent("パーティプリセット編集"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 2 - 80;
        int w = 160, h = 20;

        this.addButton(new Button(cx - w/2, y, w, h,
                new StringTextComponent("デフォルト名でパーティ作成"),
                b -> PartyActions.quickCreateDefaultParty()));
        y += 25;

        this.addButton(new Button(cx - w/2, y, w, h,
                new StringTextComponent("パーティ離脱"),
                b -> PartyActions.leaveParty()));
        y += 25;

        this.addButton(new Button(cx - w/2, y, w, h,
                new StringTextComponent("フレンド招待"),
                b -> Objects.requireNonNull(this.minecraft).setScreen(new PartyFriendScreen(this))));
        y += 35;

        this.addButton(new Button(cx - w/2, y, w, h,
                new StringTextComponent("閉じる"),
                b -> this.minecraft.setScreen(null)));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}