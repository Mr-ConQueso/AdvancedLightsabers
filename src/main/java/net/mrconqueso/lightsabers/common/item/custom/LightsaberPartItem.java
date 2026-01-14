package net.mrconqueso.lightsabers.common.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrconqueso.lightsabers.common.item.ModItems;
import net.mrconqueso.lightsabers.common.item.util.ILightsaberComponent;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;
import net.mrconqueso.lightsabers.common.registry.ModRegistries;
import net.mrconqueso.lightsabers.common.util.ALConstants;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class LightsaberPartItem extends Item implements ILightsaberComponent {
    public final PartType partType;

    public LightsaberPartItem(PartType type, Properties pProperties) {
        super(pProperties);
        this.partType = type;
    }

    @Override
    public long getFingerprint(ItemStack stack, int slot) {
        LightsaberPart part = get(stack);
        if (part != null) {
            ResourceLocation key = ModRegistries.PARTS_REGISTRY.get().getKey(part);
            return key != null ? key.hashCode() : 0;
        }
        return 0;
    }

    @Override
    public boolean isCompatibleSlot(ItemStack stack, int slot) {
        return slot == partType.ordinal();
    }

    // ChestGenHooks equivalent is Loot Tables in modern MC, so getChestGenBase is skipped.
    // getSubItems is now handled by Creative Mode Tabs data generation or event handling.

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        LightsaberPart part = get(stack);
        if (part != null) {
            tooltipComponents.add(Component.translatable(part.getDescriptionId()));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public static LightsaberPart get(ItemStack itemstack) {
        if (itemstack.hasTag()) {
            CompoundTag tag = itemstack.getTag();
            if (tag.contains(ALConstants.TAG_PART, Tag.TAG_STRING)) {
                ResourceLocation rl = new ResourceLocation(tag.getString(ALConstants.TAG_PART));
                if (ModRegistries.PARTS_REGISTRY.get().containsKey(rl)) {
                    return ModRegistries.PARTS_REGISTRY.get().getValue(rl);
                }
            }
            // Add legacy numeric ID support here if needed
        }

        return null;
    }

    public static Item getItem(PartType type) {
        return switch (type) {
            case EMITTER -> ModItems.LIGHTSABER_BLADE_EMITTER.get();
            case SWITCH_SECTION -> ModItems.LIGHTSABER_SWITCH_MODULE.get();
            case BODY -> ModItems.LIGHTSABER_GRIP.get();
            case POMMEL -> ModItems.LIGHTSABER_POMMEL.get();
        };
    }

    public static ItemStack create(PartType type, LightsaberPart part) {
        ItemStack itemstack = new ItemStack(getItem(type));
        CompoundTag tag = new CompoundTag();
        ResourceLocation key = ModRegistries.PARTS_REGISTRY.get().getKey(part);
        if (key != null) {
            tag.putString(ALConstants.TAG_PART, key.toString());
        }
        itemstack.setTag(tag);

        return itemstack;
    }

    public static PartType getType(ItemStack itemstack) {
        return itemstack.getItem() instanceof LightsaberPartItem partItem ? partItem.partType : null;
    }

    public static PartType getRandomType(Random rand) {
        return PartType.values()[rand.nextInt(PartType.values().length)];
    }

    public static PartType getRandomType() {
        return getRandomType(new Random());
    }
}