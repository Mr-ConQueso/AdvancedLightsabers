package net.mrconqueso.lightsabers.common.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.block.entity.LightsaberForgeBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdvancedLightsabers.MOD_ID);

    public static final RegistryObject<BlockEntityType<LightsaberForgeBlockEntity>> LIGHTSABER_FORGE =
            BLOCK_ENTITIES.register("lightsaber_forge", () ->
                    BlockEntityType.Builder.of(LightsaberForgeBlockEntity::new,
                            ModBlocks.LIGHTSABER_FORGE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
