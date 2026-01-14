package net.mrconqueso.lightsabers.common.lightsaber;

public abstract class LightsaberPart {
    private final PartType type;
    public float height;
    public float[] glInstructions;
    public float[] crossguard;

    public LightsaberPart(PartType type) {
        this.type = type;
    }

    public LightsaberPart(PartType type, float height, float... instructions) {
        this.type = type;
        this.height = height;
        this.glInstructions = instructions;
    }

    public PartType getType() {
        return type;
    }

    public String getDescriptionId() {
        return "lightsaber_part." + getType().name().toLowerCase() + ".unknown"; // Update with actual registry name usage if needed
    }

    public LightsaberPart addCrossguard(float x, float y, float z) {
        crossguard = new float[]{x, y, z};
        return this;
    }

    public boolean hasCrossguard() {
        return crossguard != null;
    }
}