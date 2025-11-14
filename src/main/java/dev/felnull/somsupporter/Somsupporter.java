package dev.felnull.somsupporter;

import dev.felnull.somsupporter.config.KeyBind;
import dev.felnull.somsupporter.sound.SomsoundEvents;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("somsupporter")
public class Somsupporter {
    public static final String MODID = "somsupporter";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Somsupporter() {
        SomsoundEvents.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent e) -> {
            for(KeyBind keyBind : KeyBind.values()){
                ClientRegistry.registerKeyBinding(keyBind.getKeyBinding());
            }
        });
    }
}
