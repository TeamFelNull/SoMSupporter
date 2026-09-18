package dev.felnull.somsupporter;

import dev.felnull.somsupporter.sound.SomsoundEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Somsupporter implements ModInitializer {
    public static final String MODID = "somsupporter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        SomsoundEvents.register();
    }
}
