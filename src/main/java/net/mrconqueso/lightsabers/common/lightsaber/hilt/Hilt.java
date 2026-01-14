package net.mrconqueso.lightsabers.common.lightsaber.hilt;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.lightsaber.CrystalColor;
import net.mrconqueso.lightsabers.common.lightsaber.FocusingCrystal;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberData;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;

import java.util.*;

public abstract class Hilt {
    public static final int MAX_ID = 0x3F;

    // Internal Registry
    private static final Map<Integer, Hilt> ID_TO_OBJECT = new HashMap<>();
    private static final Map<ResourceLocation, Hilt> NAME_TO_OBJECT = new HashMap<>();
    private static final Map<Hilt, Integer> OBJECT_TO_ID = new HashMap<>();
    private static final List<Hilt> OBJECTS = new ArrayList<>();

    public static final Registry REGISTRY = new Registry();

    public static final Map<String, String> LEGACY_MAPPINGS = new HashMap<>();

    private ResourceLocation registryName;

    // Registry Methods integrated from FiskRegistryEntry/Namespaced
    public static void register(int id, String key, Hilt value) {
        ResourceLocation rl = new ResourceLocation(AdvancedLightsabers.MOD_ID, key);
        value.setRegistryName(rl);

        if (ID_TO_OBJECT.containsKey(id)) throw new IllegalArgumentException("ID " + id + " is already in use by " + ID_TO_OBJECT.get(id));
        if (NAME_TO_OBJECT.containsKey(rl)) throw new IllegalArgumentException("Name " + rl + " is already in use by " + NAME_TO_OBJECT.get(rl));

        ID_TO_OBJECT.put(id, value);
        NAME_TO_OBJECT.put(rl, value);
        OBJECT_TO_ID.put(value, id);
        OBJECTS.add(value);
    }

    public static void register(String key, Hilt value) {
        int id = 0;
        while (ID_TO_OBJECT.containsKey(id)) {
            id++;
        }
        if (id > MAX_ID) throw new IllegalStateException("Hilt registry is full!");
        register(id, key, value);
    }

    // Helper class to mimic the old REGISTRY object access pattern if needed,
    // or we can just point calls to Hilt static methods.
    // The previous code used Hilt.REGISTRY.getObject... so we can keep a static helper.
    public static class Registry {
        public int getIDForObject(Hilt value) {
            return Hilt.getIdFromHilt(value);
        }

        public Hilt getObjectById(int id) {
            return Hilt.getHiltById(id);
        }

        public Hilt getObject(String name) {
            return Hilt.getHiltFromName(name);
        }

        public Hilt getObject(ResourceLocation name) {
            return NAME_TO_OBJECT.get(name);
        }

        public Hilt getRandom(Random rand) {
            return OBJECTS.get(rand.nextInt(OBJECTS.size()));
        }
    }

    public final Hilt setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    public final ResourceLocation getRegistryName() {
        return registryName;
    }

    public static Hilt getHiltFromName(String key) {
        if (!key.contains(":")) {
            return NAME_TO_OBJECT.get(new ResourceLocation(AdvancedLightsabers.MOD_ID, key));
        }
        return NAME_TO_OBJECT.get(new ResourceLocation(key));
    }

    public static String getNameForHilt(Hilt value) {
        return value.getRegistryName() != null ? value.getRegistryName().toString() : null;
    }

    public static int getIdFromHilt(Hilt value) {
        return value == null ? 0 : OBJECT_TO_ID.getOrDefault(value, 0);
    }

    public static Hilt getHiltById(int id) {
        return ID_TO_OBJECT.get(id);
    }

    public abstract LightsaberPart[] getParts();

    public abstract CrystalColor getColor();

    public Type getType() {
        return Type.SINGLE;
    }

    public Collection<FocusingCrystal> getFocusingCrystals() {
        return Collections.emptyList();
    }

    public String getUnlocalizedName() {
        return "hilt." + getRegistryName().toString().replace(':', '.') + ".name";
    }

    public String getLocalizedName() {
        return I18n.get(getUnlocalizedName()).trim();
    }

    public final LightsaberPart getPart(PartType type) {
        return getParts()[type.ordinal()];
    }

    public final LightsaberData createDefault() {
        return new LightsaberData().set(getParts()).set(getColor()).set(getFocusingCrystals().toArray(new FocusingCrystal[0]));
    }

    public static class Part extends LightsaberPart {
        public Part(PartType type, float height, float... instructions) {
            super(type, height, instructions);
        }

        @Override
        public Part addCrossguard(float x, float y, float z) {
            super.addCrossguard(x, y, z);
            return this;
        }
    }

    public enum Type {
        SINGLE,
        DOUBLE;
    }
}