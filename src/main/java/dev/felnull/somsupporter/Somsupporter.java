package dev.felnull.somsupporter;

import dev.felnull.somsupporter.config.KeyBind;
import dev.felnull.somsupporter.sound.SomsoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Somsupporter.MODID)
public class Somsupporter {
    public static final String MODID = "somsupporter";

    private static final Logger LOGGER = LogManager.getLogger();

    public Somsupporter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // サウンドイベントの登録
        SomsoundEvents.SOUNDS.register(modEventBus);
    }

    // クライアント側の MOD イベントリスナー（キーバインド登録用）
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            // 1.20.4 では ClientRegistry ではなく RegisterKeyMappingsEvent を使用して登録します
            for (KeyBind keyBind : KeyBind.values()) {
                event.register(keyBind.getKeyBinding());
            }
        }
    }
}