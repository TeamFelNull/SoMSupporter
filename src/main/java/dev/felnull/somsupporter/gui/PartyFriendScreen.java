package dev.felnull.somsupporter.gui;

import dev.felnull.somsupporter.feature.party.PartyActions;
import dev.felnull.somsupporter.feature.party.PartyConfig;
import dev.felnull.somsupporter.feature.party.PartyConfigManager;
import dev.felnull.somsupporter.feature.party.PartyOnlineChecker;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class PartyFriendScreen extends Screen {

    private final Screen parent;
    private List<PartyConfig.FriendEntry> friends;

    protected PartyFriendScreen(Screen parent) {
        super(new StringTextComponent("Friends"));
        this.parent = parent;
        this.friends = new ArrayList<>(PartyConfigManager.get().friends);
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 2 - 80;
        int w = 160, h = 20;

        for (PartyConfig.FriendEntry f : friends) {
            boolean online = PartyOnlineChecker.isOnline(f.name);

            String label =
                    (online ? "§a[ON]§r " : "§7[OFF]§r ") + f.name;

            this.addButton(new Button(cx - w/2, y, w, h,
                    new StringTextComponent(label),
                    b -> {
                        if (PartyOnlineChecker.isOnline(f.name)) {
                            PartyActions.invite(f.name);
                        }
                    }));

            y += 22;
        }

        this.addButton(new Button(cx - w/2, y + 10, w, h,
                new StringTextComponent("戻る"),
                b -> this.minecraft.setScreen(parent)));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
