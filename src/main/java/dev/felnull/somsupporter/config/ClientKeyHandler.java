package dev.felnull.somsupporter.config;

import dev.felnull.somsupporter.Somsupporter;
import dev.felnull.somsupporter.feature.party.PartyActions;
import dev.felnull.somsupporter.gui.PartyMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientKeyHandler {

    private static boolean wasTrashPressed = false;
    private static boolean wasBackpackPressed = false;
    private static boolean wasPartyCreatePressed = false;
    private static boolean wasPartyInvitePressed = false;
    private static boolean wasPartySettingsPressed = false;

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        handleKey(KeyBind.OPEN_TRASH.getKeyBinding(), wasTrashPressed, () -> sendCommand("/trash"));
        wasTrashPressed = KeyBind.OPEN_TRASH.getKeyBinding().isDown();

        handleKey(KeyBind.OPEN_BACKPACK.getKeyBinding(), wasBackpackPressed, () -> sendCommand("/backpack"));
        wasBackpackPressed = KeyBind.OPEN_BACKPACK.getKeyBinding().isDown();

        handleKey(KeyBind.QUICK_PARTY_CREATE.getKeyBinding(), wasPartyCreatePressed, PartyActions::quickCreateDefaultParty);
        wasPartyCreatePressed = KeyBind.QUICK_PARTY_CREATE.getKeyBinding().isDown();

        handleKey(KeyBind.QUICK_PARTY_INVITE.getKeyBinding(), wasPartyInvitePressed, PartyActions::quickInviteDefaultFriend);
        wasPartyInvitePressed = KeyBind.QUICK_PARTY_INVITE.getKeyBinding().isDown();

        handleKey(KeyBind.OPEN_PARTY_SETTINGS.getKeyBinding(), wasPartySettingsPressed, () -> mc.setScreen(new PartyMainScreen()));
        wasPartySettingsPressed = KeyBind.OPEN_PARTY_SETTINGS.getKeyBinding().isDown();
    }

    private static void handleKey(KeyBinding key, boolean wasDown, Runnable action) {
        boolean now = key.isDown();
        if (now && !wasDown) {
            action.run();
        }
    }

    private static void sendCommand(String cmd) {
        Minecraft mc = Minecraft.getInstance();
        if (!cmd.startsWith("/")) cmd = "/" + cmd;
        try {
            mc.player.chat(cmd);
        } catch (Throwable ex) {
            try { mc.player.chat(cmd); } catch (Throwable ignored) {}
        }
    }
}