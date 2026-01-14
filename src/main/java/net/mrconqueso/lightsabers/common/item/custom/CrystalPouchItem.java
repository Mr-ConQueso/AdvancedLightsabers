package net.mrconqueso.lightsabers.common.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrconqueso.lightsabers.common.lightsaber.CrystalColor;
import net.mrconqueso.lightsabers.common.util.ALConstants;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CrystalPouchItem extends Item {
    public static final UUID NULL_UUID = UUID.randomUUID();

    public CrystalPouchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Ensure UUID exists before opening
            getUUID(stack);
            
            // TODO: Implement MenuProvider and Container for the pouch
            // NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
            //     (id, inventory, p) -> new CrystalPouchMenu(id, inventory, stack), 
            //     stack.getHoverName()
            // ), buf -> buf.writeItem(stack));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            getUUID(stack); // Ensure UUID is assigned
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(getColor(stack).getUnlocalizedName()));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    // Color logic reused from CrystalItem but applied to Pouch
    public static CrystalColor getColor(ItemStack stack) {
        // Reuse CrystalItem's ID logic if possible, or duplicate for independence
        return CrystalItem.get(stack);
    }
    
    public static boolean isPouch(ItemStack stack) {
        return stack.getItem() instanceof CrystalPouchItem;
    }

    public static UUID getUUID(ItemStack stack) {
        if (!isPouch(stack)) {
            return NULL_UUID;
        }

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ALConstants.TAG_POUCH_UUID)) {
            UUID uuid = UUID.randomUUID();
            tag.putString(ALConstants.TAG_POUCH_UUID, uuid.toString());
            return uuid;
        }

        try {
            return UUID.fromString(tag.getString(ALConstants.TAG_POUCH_UUID));
        } catch (IllegalArgumentException e) {
            return NULL_UUID;
        }
    }
}
