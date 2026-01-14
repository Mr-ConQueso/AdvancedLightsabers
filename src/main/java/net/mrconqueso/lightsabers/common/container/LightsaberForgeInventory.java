package net.mrconqueso.lightsabers.common.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.mrconqueso.lightsabers.common.item.custom.CrystalItem;
import net.mrconqueso.lightsabers.common.item.custom.FocusingCrystalItem;
import net.mrconqueso.lightsabers.common.item.custom.LightsaberPartItem;
import net.mrconqueso.lightsabers.common.item.util.ILightsaberComponent;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberData;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;

public class LightsaberForgeInventory {
    
    // Slot Constants
    public static final int SLOT_EMITTER = 0;
    public static final int SLOT_SWITCH = 1;
    public static final int SLOT_BODY = 2;
    public static final int SLOT_POMMEL = 3;
    public static final int SLOT_COLOR = 4;
    public static final int SLOT_FOCUS_1 = 5;
    public static final int SLOT_FOCUS_2 = 6;

    public static LightsaberData calculateResult(ItemStackHandler inventory) {
        LightsaberData data = new LightsaberData();

        // 1. Check Parts
        if (!addPart(data, inventory.getStackInSlot(SLOT_EMITTER), PartType.EMITTER)) return null;
        if (!addPart(data, inventory.getStackInSlot(SLOT_SWITCH), PartType.SWITCH_SECTION)) return null;
        if (!addPart(data, inventory.getStackInSlot(SLOT_BODY), PartType.BODY)) return null;
        if (!addPart(data, inventory.getStackInSlot(SLOT_POMMEL), PartType.POMMEL)) return null;

        // 2. Check Color
        ItemStack colorStack = inventory.getStackInSlot(SLOT_COLOR);
        if (colorStack.getItem() instanceof CrystalItem) {
             data.set(CrystalItem.get(colorStack));
        } else {
            return null; // Color is mandatory
        }

        // 3. Focusing Crystals (Optional)
        addFocusing(data, inventory.getStackInSlot(SLOT_FOCUS_1));
        addFocusing(data, inventory.getStackInSlot(SLOT_FOCUS_2));

        return data;
    }

    private static boolean addPart(LightsaberData data, ItemStack stack, PartType expectedType) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() instanceof LightsaberPartItem partItem) {
            if (partItem.partType == expectedType) {
                data.set(expectedType, LightsaberPartItem.get(stack));
                return true;
            }
        }
        return false;
    }

    private static void addFocusing(LightsaberData data, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof FocusingCrystalItem) {
            // Assuming FocusingCrystalItem has a static get() method similar to CrystalItem
            // data.add(FocusingCrystalItem.get(stack));
        }
    }
}