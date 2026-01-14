package net.mrconqueso.lightsabers.common.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrconqueso.lightsabers.common.item.util.ILightsaberComponent;
import net.mrconqueso.lightsabers.common.lightsaber.CrystalColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalItem extends Item implements ILightsaberComponent {
    private static final String TAG_COLOR = "color";

    public CrystalItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public long getFingerprint(ItemStack stack, int slot) {
        return (getId(stack) & 0xFF) << 24;
    }

    @Override
    public boolean isCompatibleSlot(ItemStack stack, int slot) {
        // Assuming slot 5 is the crystal slot in your container logic
        return slot == 5;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        // Use the localized name from CrystalColor (ensure CrystalColor updates use I18n on client or Component)
        tooltipComponents.add(Component.translatable(get(stack).getUnlocalizedName()));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        // Map 1.7.10 EnumRarity (common, uncommon, rare, epic) to 1.20.1 Rarity
        CrystalColor color = get(stack);
        // Simple mapping based on your old groups, or default to COMMON
        if (color == CrystalColor.WHITE || color == CrystalColor.ARCTIC_BLUE) return Rarity.EPIC;
        if (color == CrystalColor.PURPLE || color == CrystalColor.INDIGO || color == CrystalColor.CYAN) return Rarity.RARE;
        if (color == CrystalColor.MAGENTA || color == CrystalColor.PINK || color == CrystalColor.RED || color == CrystalColor.BLOOD_ORANGE) return Rarity.UNCOMMON;
        return Rarity.COMMON;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            // Legacy conversion: if "color" tag exists but is directly in root (rare in modern), move it?
            // Current get() logic handles NBT reading.
        }
    }

    /**
     * Gets the color ID from the item stack's NBT.
     * Defaults to 0 (First enum value) if not present.
     */
    public static int getId(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(TAG_COLOR)) {
            return stack.getTag().getInt(TAG_COLOR);
        }
        return 0;
    }

    public static CrystalColor get(ItemStack stack) {
        return CrystalColor.get(getId(stack));
    }

    public static ItemStack create(CrystalColor color) {
        // Create an item with the specific color ID in NBT
        // Assuming ModItems.CRYSTAL exists and refers to this item
        // You'll need to pass the Item reference or use "this" if calling from instance context,
        // but this is static.
        // For now, returning ItemStack.EMPTY or needing a reference to the registered Item.
        // Ideally, call: new ItemStack(ModItems.CRYSTAL.get()) and set tag.
        
        // Since we are inside the class, we can't easily reference the RegistryObject without circular dep
        // unless passed in.
        // Typically, usage is: CrystalItem.create(ModItems.CRYSTAL.get(), color);
        return ItemStack.EMPTY; 
    }
    
    public static ItemStack create(Item item, CrystalColor color) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_COLOR, color.id);
        return stack;
    }

    // Loot generation logic (getChestGenBase) is removed. 
    // This must be implemented via Global Loot Modifiers or Loot Tables JSONs.
}
