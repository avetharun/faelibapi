package dev.feintha.apis.itemrenderingapi.ModelOverrides;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function4;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MapModelOverride extends StringModelOverride{

    public MapModelOverride(Function4<ItemStack, ClientWorld, LivingEntity, Integer, String> p) {
        super(p);
    }

    public static class MapModelOverrideCondition extends ModelOverride.Condition {
        public final JsonObject value;
        public MapModelOverrideCondition(Identifier type, JsonObject value) {
            super(type, 0.01f);
            this.value = value;
        }
    }
    public static class InlinedMapModelOverrideCondition extends ModelOverrideList.InlinedCondition {
        public final JsonObject value;
        public InlinedMapModelOverrideCondition(int type, JsonObject value) {
            super(type, 0.01f);
            this.value = value;
        }
    }
}
