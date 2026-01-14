package net.mrconqueso.lightsabers.common.lightsaber;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.mrconqueso.lightsabers.common.item.ModItems;
import net.mrconqueso.lightsabers.common.item.custom.DoubleLightsaberItem;
import net.mrconqueso.lightsabers.common.lightsaber.builder.AbstractLightsaberData;
import net.mrconqueso.lightsabers.common.registry.ModRegistries;
import net.mrconqueso.lightsabers.common.util.ALConstants;

import java.util.Random;

public class LightsaberData extends AbstractLightsaberData implements INBTSerializable<Tag> {
    public static final LightsaberData EMPTY = new LightsaberData();
    public static final float MIN_LENGTH_CM = 19;

    public LightsaberData() {
        super();
    }

    @Override
    protected AbstractLightsaberData createNew() {
        return new LightsaberData();
    }

    @Override
    public LightsaberData copy() {
        return (LightsaberData) super.copy();
    }

    public boolean isTooShort() {
        return getHeightCm() < MIN_LENGTH_CM;
    }

    public float[] getRGB(ItemStack itemstack) {
        if (itemstack.getHoverName().getString().equals("jeb_") && FMLEnvironment.dist == Dist.CLIENT) {
            return getJebColor();
        }

        return getColor().getRGB();
    }

    private float[] getJebColor() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int time = 25;
            float[][] rgb = CrystalColor.COLOR_VALUES;
            float partialTicks = Minecraft.getInstance().getFrameTime();

            float f = (player.tickCount % time + partialTicks) / time;
            int i = player.tickCount / time;
            int j = i % rgb.length;
            int k = (i + 1) % rgb.length;

            return new float[]{
                    rgb[j][0] * (1 - f) + rgb[k][0] * f,
                    rgb[j][1] * (1 - f) + rgb[k][1] * f,
                    rgb[j][2] * (1 - f) + rgb[k][2] * f
            };
        }
        return getColor().getRGB();
    }

    @Override
    public LightsaberData set(PartType type, LightsaberPart part) {
        return (LightsaberData) super.set(type, part);
    }

    @Override
    public LightsaberData set(LightsaberPart... parts) {
        return (LightsaberData) super.set(parts);
    }

    @Override
    public LightsaberData set(LightsaberPart part) {
        return (LightsaberData) super.set(part);
    }

    @Override
    public LightsaberData set(CrystalColor color) {
        return (LightsaberData) super.set(color);
    }

    @Override
    public LightsaberData set(FocusingCrystal... crystals) {
        return (LightsaberData) super.set(crystals);
    }

    @Override
    public LightsaberData add(FocusingCrystal crystal) {
        return (LightsaberData) super.add(crystal);
    }

    @Override
    public LightsaberData remove(FocusingCrystal crystal) {
        return (LightsaberData) super.remove(crystal);
    }

    public ItemStack create() {
        ItemStack itemstack = new ItemStack(ModItems.LIGHTSABER.get());
        itemstack.setTag(new CompoundTag());
        itemstack.getTag().put(net.mrconqueso.lightsabers.common.util.ALConstants.TAG_LIGHTSABER, serializeNBT());

        return itemstack;
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (PartType type : PartType.values()) {
            LightsaberPart part = get(type);
            if (part != null) {
                ResourceLocation key = ModRegistries.PARTS_REGISTRY.get().getKey(part);
                if (key != null) {
                    tag.putString(type.name(), key.toString());
                }
            }
        }
        tag.putInt("Color", getColor().id);

        ListTag crystals = new ListTag();
        for (FocusingCrystal crystal : getFocusingCrystals()) {
            crystals.add(StringTag.valueOf(crystal.name()));
        }
        tag.put("FocusingCrystals", crystals);

        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag tag) {
            for (PartType type : PartType.values()) {
                if (tag.contains(type.name())) {
                    ResourceLocation rl = new ResourceLocation(tag.getString(type.name()));
                    if (ModRegistries.PARTS_REGISTRY.get().containsKey(rl)) {
                        set(type, ModRegistries.PARTS_REGISTRY.get().getValue(rl));
                    }
                }
            }
            if (tag.contains("Color")) {
                set(CrystalColor.get(tag.getInt("Color")));
            }
            if (tag.contains("FocusingCrystals")) {
                ListTag crystals = tag.getList("FocusingCrystals", Tag.TAG_STRING);
                for (int i = 0; i < crystals.size(); i++) {
                    try {
                        add(FocusingCrystal.valueOf(crystals.getString(i)));
                    } catch (IllegalArgumentException e) {
                        // ignore invalid crystals
                    }
                }
            }
        }
    }

//    @Override
//    public void toBytes(io.netty.buffer.ByteBuf buf) {
//        // Implement network sync using NBT logic if needed
//    }

    public static LightsaberData readFromNBT(CompoundTag nbt) {
        if (nbt.contains(ALConstants.TAG_LIGHTSABER)) {
            LightsaberData data = new LightsaberData();
            data.deserializeNBT(nbt.get(ALConstants.TAG_LIGHTSABER));
            return data;
        } else if (nbt.contains("Lightsaber", Tag.TAG_COMPOUND)) {
            // Legacy support for migration
            return new LightsaberData(); 
        }

        return EMPTY;
    }

    public static LightsaberData get(ItemStack itemstack) {
        if (itemstack != null && itemstack.hasTag()) {
            return readFromNBT(itemstack.getTag());
        }

        return EMPTY;
    }

    public static LightsaberPart get(ItemStack itemstack, PartType type) {
        return get(itemstack).get(type);
    }

    public static LightsaberPart[] getHilt(ItemStack itemstack) {
        return get(itemstack).getParts();
    }

    public static LightsaberPart getPart(ItemStack itemstack, PartType type) {
        return get(itemstack).getPart(type);
    }

    public static float getHeight(ItemStack itemstack) {
        if (itemstack.getItem() == ModItems.DOUBLE_LIGHTSABER.get()) {
            LightsaberData[] array = DoubleLightsaberItem.get(itemstack);
            return array[0].getHeight() + array[1].getHeight();
        }

        return get(itemstack).getHeight();
    }

    public static float getHeightCm(ItemStack itemstack) {
        return getHeight(itemstack) * 0.575F;
    }

    public static CrystalColor getColor(ItemStack itemstack) {
        return get(itemstack).getColor();
    }

    public static ItemStack createRandom(Random rand, CrystalColor color) {
        return new ItemStack(ModItems.LIGHTSABER.get());
    }

    public static ItemStack createRandom(Random rand) {
        return createRandom(rand, null);
    }
}