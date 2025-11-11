package dev.felnull.somsupporter.config;

import dev.felnull.somsupporter.Somsupporter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientKeyHandler {

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (KeyBindings.RUN_COMMAND.consumeClick()) {
            // 実行したいコマンドを固定で書く
            String cmd = "/backpack";  // ← ここを好きなコマンドに
            if (!cmd.startsWith("/")) cmd = "/" + cmd;

            // チャット経由で送信（バージョン差対応）
            try {
                mc.player.chat(cmd); // MCP
            } catch (Throwable ex) {
                try { mc.player.chat(cmd); } catch (Throwable ignored) {}
            }
        }
    }
}