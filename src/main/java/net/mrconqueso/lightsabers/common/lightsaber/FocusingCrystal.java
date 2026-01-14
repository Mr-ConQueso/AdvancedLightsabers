package net.mrconqueso.lightsabers.common.lightsaber;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum FocusingCrystal
{
    COMPRESSED,
    CRACKED,
    INVERTING,
    FINE_CUT,
//    CHARGED,
    PRISMATIC;
    
    public long getCode()
    {
        return (1 << ordinal()) & 0xFFFF;
    }

    public String getUnlocalizedName()
    {
        return "lightsaber.focusingCrystal." + name().toLowerCase(Locale.ROOT);
    }
    public MutableComponent getLocalizedName()
    {
        return Component.translatable(getUnlocalizedName().trim());
    }
}
