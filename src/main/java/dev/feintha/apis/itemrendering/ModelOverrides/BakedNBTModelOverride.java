package dev.feintha.apis.itemrenderingapi.ModelOverrides;

import net.minecraft.nbt.NbtCompound;

// This class should really be named something like "DynamicNBTModelOverride" as it always returns false, however the parser can handle nbt as strings
public class BakedNBTModelOverride extends BooleanModelOverride {
    public NbtCompound nbtRequest;
    public BakedNBTModelOverride(){
        // Handled via mixin!!!
        super((itemStack, clientWorld, livingEntity, integer) -> false );
    }
}
