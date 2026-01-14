package net.mrconqueso.lightsabers.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;

import java.util.function.Supplier;

public class ModRegistries {
    public static final DeferredRegister<LightsaberPart> PARTS = DeferredRegister.create(new ResourceLocation(AdvancedLightsabers.MOD_ID, "parts"), AdvancedLightsabers.MOD_ID);

    public static final Supplier<IForgeRegistry<LightsaberPart>> PARTS_REGISTRY = PARTS.makeRegistry(RegistryBuilder::new);

    public static void register(IEventBus eventBus) {
        PARTS.register(eventBus);
    }
}