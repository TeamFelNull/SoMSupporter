package dev.felnull.somsupporter.sound;

import dev.felnull.somsupporter.Somsupporter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SomsoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Somsupporter.MODID);

    public static final RegistryObject<SoundEvent> NOTIFY_LOWCHANCE =
            SOUNDS.register("notify_lowchance",
                    () -> new SoundEvent(new ResourceLocation(Somsupporter.MODID, "notify_lowchance")));
}