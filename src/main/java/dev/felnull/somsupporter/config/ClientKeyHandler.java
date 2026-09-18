package dev.felnull.somsupporter.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientKeyHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.screen != null) return;

            while (KeyBind.OPEN_TRASH.getKeyBinding().consumeClick()) {
                sendCommand("trash");
            }
            while (KeyBind.OPEN_BACKPACK.getKeyBinding().consumeClick()) {
                sendCommand("backpack");
            }
        });
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
