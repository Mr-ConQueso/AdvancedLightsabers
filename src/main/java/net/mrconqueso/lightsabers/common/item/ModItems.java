package net.mrconqueso.lightsabers.common.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.item.custom.*;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;
import net.mrconqueso.lightsabers.datagen.content.SimpleItemModel;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AdvancedLightsabers.MOD_ID);


    public static final RegistryObject<Item> KYBER_CRYSTAL = registerItem("kyber_crystal",
            () -> new CrystalItem(new Item.Properties().stacksTo(8)));

    public static final RegistryObject<Item> CIRCUITRY = registerItem("lightsaber_circuitry",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FOCUSING_CRYSTAL = registerItem("focusing_crystal_fine_cut",
            () -> new FocusingCrystalItem(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTAL_POUCH = registerItem("crystal_pouch",
            () -> new CrystalPouchItem(new Item.Properties()));

    public static final RegistryObject<Item> LIGHTSABER_BLADE_EMITTER = registerItemNoModel("lightsaber_blade_emitter",
            () -> new LightsaberPartItem(PartType.EMITTER, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> LIGHTSABER_SWITCH_MODULE = registerItemNoModel("lightsaber_switch_module",
            () -> new LightsaberPartItem(PartType.SWITCH_SECTION, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> LIGHTSABER_GRIP = registerItemNoModel("lightsaber_grip",
            () -> new LightsaberPartItem(PartType.BODY, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> LIGHTSABER_POMMEL = registerItemNoModel("lightsaber_pommel",
            () -> new LightsaberPartItem(PartType.POMMEL, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> LIGHTSABER = registerItemNoModel("lightsaber",
            () -> new LightsaberItem(new Item.Properties()));
    public static final RegistryObject<Item> DOUBLE_LIGHTSABER = registerItemNoModel("double_lightsaber",
            () -> new DoubleLightsaberItem(new Item.Properties()));


    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> itemSupplier) {
        RegistryObject<T> registeredItem = ITEMS.register(name, itemSupplier);
        ModItemGroups.MISC_CONTENTS_ITEMS.add(registeredItem);
        SimpleItemModel.add(registeredItem);
        return registeredItem;
    }

    private static <T extends Item> RegistryObject<T> registerItemNoModel(String name, Supplier<T> itemSupplier) {
        RegistryObject<T> registeredItem = ITEMS.register(name, itemSupplier);
        ModItemGroups.MISC_CONTENTS_ITEMS.add(registeredItem);
        return registeredItem;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
