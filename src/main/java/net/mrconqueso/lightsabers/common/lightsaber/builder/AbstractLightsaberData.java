package net.mrconqueso.lightsabers.common.lightsaber.builder;

import net.mrconqueso.lightsabers.common.lightsaber.CrystalColor;
import net.mrconqueso.lightsabers.common.lightsaber.FocusingCrystal;
import net.mrconqueso.lightsabers.common.lightsaber.LightsaberPart;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLightsaberData {
    protected final LightsaberPart[] parts = new LightsaberPart[4];
    protected CrystalColor color = CrystalColor.WHITE;
    protected final List<FocusingCrystal> focusingCrystals = new ArrayList<>();

    public AbstractLightsaberData() {
    }

    public AbstractLightsaberData copy() {
        AbstractLightsaberData copy = createNew();
        System.arraycopy(this.parts, 0, copy.parts, 0, 4);
        copy.color = this.color;
        copy.focusingCrystals.addAll(this.focusingCrystals);
        return copy;
    }

    protected abstract AbstractLightsaberData createNew();

    public LightsaberPart get(PartType type) {
        return parts[type.ordinal()];
    }

    public LightsaberPart[] getParts() {
        return parts;
    }

    public LightsaberPart getPart(PartType type) {
        return get(type);
    }

    public float getHeight() {
        float height = 0;
        for (PartType type : PartType.values()) {
            LightsaberPart part = getPart(type);
            if (part != null) {
                height += part.height;
            }
        }
        return height;
    }

    public float getHeightCm() {
        return getHeight() * 0.575F;
    }

    public CrystalColor getColor() {
        return color;
    }

    public FocusingCrystal[] getFocusingCrystals() {
        return focusingCrystals.toArray(new FocusingCrystal[0]);
    }

    public boolean hasFocusingCrystal(FocusingCrystal crystal) {
        return focusingCrystals.contains(crystal);
    }

    public AbstractLightsaberData set(PartType type, LightsaberPart part) {
        parts[type.ordinal()] = part;
        return this;
    }

    public AbstractLightsaberData set(LightsaberPart part) {
        for (int i = 0; i < parts.length; i++) {
            parts[i] = part;
        }
        return this;
    }

    public AbstractLightsaberData set(LightsaberPart... parts) {
        for (int i = 0; i < Math.min(this.parts.length, parts.length); i++) {
            this.parts[i] = parts[i];
        }
        return this;
    }

    public AbstractLightsaberData set(CrystalColor color) {
        this.color = color;
        return this;
    }

    public AbstractLightsaberData set(FocusingCrystal... crystals) {
        this.focusingCrystals.clear();
        for (FocusingCrystal crystal : crystals) {
            if (crystal != null) {
                add(crystal);
            }
        }
        return this;
    }

    public AbstractLightsaberData add(FocusingCrystal crystal) {
        if (!focusingCrystals.contains(crystal)) {
            focusingCrystals.add(crystal);
        }
        return this;
    }

    public AbstractLightsaberData remove(FocusingCrystal crystal) {
        focusingCrystals.remove(crystal);
        return this;
    }
}
