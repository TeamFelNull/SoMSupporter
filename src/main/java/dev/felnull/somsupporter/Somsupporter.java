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
        SomsoundEvents.SOUNDS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            for (KeyBind keyBind : KeyBind.values()) {
                event.register(keyBind.getKeyBinding());
            }
        }
    }
}