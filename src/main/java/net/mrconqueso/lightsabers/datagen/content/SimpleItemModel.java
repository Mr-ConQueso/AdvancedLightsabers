package net.mrconqueso.lightsabers.datagen.content;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemModel {
    // We use RegistryObject because items are not initialized during ModItems static init
    public static final List<RegistryObject<? extends Item>> SIMPLE_ITEMS = new ArrayList<>();

    // Keep the old list for compatibility if needed, but it won't be populated correctly via static init
    public static final List<Item> items = new ArrayList<>(); 

    public static void add(RegistryObject<? extends Item> item) {
        SIMPLE_ITEMS.add(item);
    }
}
