package net.mrconqueso.lightsabers.common.item.custom;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrconqueso.lightsabers.common.item.util.ILightsaberComponent;
import net.mrconqueso.lightsabers.common.lightsaber.FocusingCrystal;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberData;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;
import net.mrconqueso.lightsabers.common.util.ALConstants;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightsaberItem extends Item implements ILightsaberComponent {
    public LightsaberItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(ALConstants.TAG_LIGHTSABER_SPECIAL, Tag.TAG_STRING)) {
            return Component.literal("FISHSTICKS!!");
        }
        return super.getName(stack);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(ALConstants.TAG_LIGHTSABER_SPECIAL, Tag.TAG_STRING)) {
            return Rarity.RARE;
        }
        return Rarity.COMMON;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        LightsaberData data = get(stack);
        LightsaberPart[] parts = data.getParts();
        String space = "  ";

        tooltipComponents.add(Component.translatable("lightsaber.color"));
        tooltipComponents.add(Component.literal(space).append(Component.translatable(data.getColor().getUnlocalizedName())));
        tooltipComponents.add(Component.translatable("lightsaber.hilt"));

        // Simplified logic: Just list parts found. 
        // In 1.20 we rely on Components for translation
        for (LightsaberPart part : parts) {
            if (part != null) {
                tooltipComponents.add(Component.literal(space).append(Component.translatable(part.getDescriptionId())));
            }
        }

        FocusingCrystal[] crystals = data.getFocusingCrystals();
        if (crystals.length > 0) {
            tooltipComponents.add(Component.translatable("lightsaber.focusingCrystals"));
            for (FocusingCrystal crystal : crystals) {
                tooltipComponents.add(Component.literal(space + space + crystal.getLocalizedName()));
            }
        }
    }
    
    // ILightsaberComponent implementation
    @Override
    public long getFingerprint(ItemStack stack, int slot) {
        return 0; // Not used for full lightsabers usually
    }

    @Override
    public boolean isCompatibleSlot(ItemStack stack, int slot) {
        return false;
    }

    public static LightsaberData get(ItemStack itemstack) {
        return LightsaberData.get(itemstack);
    }
}
