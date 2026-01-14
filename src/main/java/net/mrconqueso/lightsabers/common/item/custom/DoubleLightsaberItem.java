package net.mrconqueso.lightsabers.common.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrconqueso.lightsabers.common.item.ModItems;
import net.mrconqueso.lightsabers.common.lightsaber.FocusingCrystal;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberData;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DoubleLightsaberItem extends Item {
    public DoubleLightsaberItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        LightsaberData[] array = get(stack);
        String space = "  ";

        tooltipComponents.add(Component.translatable("lightsaber.lightsabers.color"));
        tooltipComponents.add(Component.literal(space).append(Component.translatable(array[0].getColor().getUnlocalizedName())));

        if (array[0].getColor() != array[1].getColor()) {
            tooltipComponents.add(Component.literal(space + array[1].getColor().getLocalizedName()));
        }

        tooltipComponents.add(Component.translatable("lightsaber.hilt"));
        
        // Simplified listing for both ends
        for(int i=0; i<2; i++) {
             tooltipComponents.add(Component.literal(space + (i==0 ? "Upper:" : "Lower:")));
             for (LightsaberPart part : array[i].getParts()) {
                 if (part != null) {
                    tooltipComponents.add(Component.literal(space + space).append(Component.translatable(part.getDescriptionId())));
                 }
             }
        }

        FocusingCrystal[][] crystals = {array[0].getFocusingCrystals(), array[1].getFocusingCrystals()};
        String[] astring = {"upper", "lower"};
        
        if (crystals[0].length > 0 || crystals[1].length > 0) {
             tooltipComponents.add(Component.translatable("lightsaber.focusingCrystals"));
        }
        
        for (int i = 0; i < astring.length; ++i) {
            if (crystals[i].length > 0) {
                tooltipComponents.add(Component.literal(space).append(Component.translatable("lightsaber." + astring[i])));
                for (FocusingCrystal crystal : crystals[i]) {
                    tooltipComponents.add(Component.literal(space + space + crystal.getLocalizedName()));
                }
            }
        }
    }

    public static LightsaberData[] get(ItemStack itemstack) {
        LightsaberData[] data = new LightsaberData[] {
            new LightsaberData(),
            new LightsaberData()
        };

        if (itemstack.hasTag()) {
            CompoundTag compound = itemstack.getTag();
            // Supports both legacy structure (if migrated) and new structure
            if (compound.contains("Upper", Tag.TAG_COMPOUND)) {
                 data[0] = LightsaberData.readFromNBT(compound.getCompound("Upper"));
            }
            if (compound.contains("Lower", Tag.TAG_COMPOUND)) {
                 data[1] = LightsaberData.readFromNBT(compound.getCompound("Lower"));
            }
        }
    
        return data;
    }

    public static ItemStack create(LightsaberData[] array) {
        ItemStack itemstack = new ItemStack(ModItems.DOUBLE_LIGHTSABER.get());
        CompoundTag tag = new CompoundTag();
        
        tag.put("Upper", array[0].serializeNBT());
        tag.put("Lower", array[1].serializeNBT());
        
        itemstack.setTag(tag);
        return itemstack;
    }
    
    public static ItemStack create(ItemStack upper, ItemStack lower) {
        return create(new LightsaberData[] {LightsaberData.get(upper), LightsaberData.get(lower)});
    }
}
