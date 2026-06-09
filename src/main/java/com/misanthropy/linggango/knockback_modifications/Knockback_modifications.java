package com.misanthropy.linggango.knockback_modifications;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Knockback_modifications.MODID)
public class Knockback_modifications {
    public static final String MODID = "knockback_modifications";

    public Knockback_modifications() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}