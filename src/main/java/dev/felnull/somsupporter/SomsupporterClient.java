package dev.felnull.somsupporter;

import dev.felnull.somsupporter.config.ClientKeyHandler;
import dev.felnull.somsupporter.config.KeyBind;
import dev.felnull.somsupporter.listener.ClientEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SomsupporterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBind.register();
        ClientEvents.register();
        ClientKeyHandler.register();
        HudPanelRenderer.register();
    }
}
