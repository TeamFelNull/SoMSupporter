package dev.felnull.somsupporter.sound;

import dev.felnull.somsupporter.Somsupporter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Somsupporter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SomsoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Somsupporter.MODID);

    public static final RegistryObject<SoundEvent> NOTIFY_LOWCHANCE =
            SOUNDS.register("notify_lowchance",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Somsupporter.MODID, "notify_lowchance")));

    // メインModクラスのコンストラクタなどから呼び出して登録する場合用
    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}