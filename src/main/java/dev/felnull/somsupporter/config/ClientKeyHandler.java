package dev.felnull.somsupporter.config;

import dev.felnull.somsupporter.Somsupporter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientKeyHandler {

    private static boolean wasTrashPressed = false;
    private static boolean wasBackpackPressed = false;

    @SubscribeEvent
    public static void onKey(InputEvent.Key e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        handleKey(KeyBind.OPEN_TRASH.getKeyBinding(), wasTrashPressed, () -> sendCommand("trash"));
        wasTrashPressed = KeyBind.OPEN_TRASH.getKeyBinding().isDown();

        handleKey(KeyBind.OPEN_BACKPACK.getKeyBinding(), wasBackpackPressed, () -> sendCommand("backpack"));
        wasBackpackPressed = KeyBind.OPEN_BACKPACK.getKeyBinding().isDown();

    }

    private static void handleKey(KeyMapping key, boolean wasDown, Runnable action) {
        boolean now = key.isDown();
        if (now && !wasDown) {
            action.run();
        }
    }

    private static void sendCommand(String cmd) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.connection == null) return;

        if (cmd.startsWith("/")) {
            cmd = cmd.substring(1);
        }

        mc.player.connection.sendUnsignedCommand(cmd);
    }
}