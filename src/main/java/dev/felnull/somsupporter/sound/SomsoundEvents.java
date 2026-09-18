package dev.felnull.somsupporter.sound;

import dev.felnull.somsupporter.Somsupporter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SomsoundEvents {
    public static final ResourceLocation NOTIFY_LOWCHANCE_ID =
            ResourceLocation.fromNamespaceAndPath(Somsupporter.MODID, "notify_lowchance");

    public static final SoundEvent NOTIFY_LOWCHANCE =
            SoundEvent.createVariableRangeEvent(NOTIFY_LOWCHANCE_ID);

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, NOTIFY_LOWCHANCE_ID, NOTIFY_LOWCHANCE);
    }
}
