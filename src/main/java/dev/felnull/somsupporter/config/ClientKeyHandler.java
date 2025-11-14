package dev.felnull.somsupporter.config;

import dev.felnull.somsupporter.Somsupporter;
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
    public static void onKey(InputEvent.KeyInputEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Deleteキー：Trash
        boolean nowEquip = KeyBind.OPEN_TRASH.getKeyBinding().isDown();
        if (nowEquip && !wasTrashPressed) {
            sendCommand("/trash");
        }
        wasTrashPressed = nowEquip;

        // Bキー：BackPack
        boolean nowBackpack = KeyBind.OPEN_BACKPACK.getKeyBinding().isDown();
        if (nowBackpack && !wasBackpackPressed) {
            sendCommand("/backpack");
        }
        wasBackpackPressed = nowBackpack;
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