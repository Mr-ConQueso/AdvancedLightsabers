package net.mrconqueso.lightsabers.common.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdvancedLightsabers.MOD_ID);

    public static final List<Supplier<? extends net.minecraft.world.item.Item>> MISC_CONTENTS_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> MISC_CONTENTS = CREATIVE_MODE_TABS.register("misc_contents",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.LIGHTSABER.get())) // Use a representative icon
                    .title(Component.translatable("creativetab." + AdvancedLightsabers.MOD_ID + ".misc_contents"))
                    .displayItems((pParameters, pOutput) -> {
                        // Add all items registered to this list
                        for (Supplier<? extends net.minecraft.world.item.Item> item : MISC_CONTENTS_ITEMS) {
                            pOutput.accept(item.get());
                        }

                        pOutput.accept(ModBlocks.LIGHTSABER_FORGE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
