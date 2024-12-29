package dev.feintha.apis.itemrenderingapi.mixin;

import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.feintha.apis.cfr.alib;
import dev.feintha.apis.itemrenderingapi.ModelOverrides.MapModelOverride;
import dev.feintha.apis.itemrenderingapi.ModelOverrides.StringModelOverride;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelOverrideList.class)
public abstract class ModelOverrideListMixin {
    @Redirect(method = "<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V", at=@At(value = "NEW", target = "([Lnet/minecraft/client/render/model/json/ModelOverrideList$InlinedCondition;Lnet/minecraft/client/render/model/BakedModel;)Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;"))
    ModelOverrideList.BakedOverride createCustomInlinedCondition(ModelOverrideList.InlinedCondition[] inlinedConditions, BakedModel model, @Local ModelOverride modelOverride, @Local BakedModel bakedModel, @Local Object2IntMap<Identifier> object2IntMap) {
        inlinedConditions = modelOverride.streamConditions().map((condition) -> {
            int i = object2IntMap.getInt(condition.getType());
            if (condition instanceof StringModelOverride.StringModelOverrideCondition sMOC) {
                return new StringModelOverride.InlinedStringModelOverrideCondition(i, sMOC.value);
            } else if (condition instanceof MapModelOverride.MapModelOverrideCondition mapCondition) {
                return new MapModelOverride.InlinedMapModelOverrideCondition(i, mapCondition.value);
            } else {
                return new ModelOverrideList.InlinedCondition(i, condition.getThreshold());
            }
        }).toArray(ModelOverrideList.InlinedCondition[]::new);
        return new ModelOverrideList.BakedOverride(inlinedConditions, bakedModel);
    }
    @ModifyExpressionValue(
            method="apply", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;test([F)Z"))
    private boolean applyMixin(boolean original, @Local ModelOverrideList.BakedOverride override, @Local(argsOnly = true) ItemStack stack){
        if (!original) {
            for (ModelOverrideList.InlinedCondition condition : override.conditions) {
//                if (condition instanceof MapModelOverride.InlinedMapModelOverrideCondition) {
//                    return ;
//                }
                if (condition instanceof StringModelOverride.InlinedStringModelOverrideCondition stringCondition) {
                    if (stringCondition.value.startsWith("{") && stringCondition.value.endsWith("}")) {
                        NbtCompound c1 = alib.json2NBT(JsonParser.parseString(stringCondition.value).getAsJsonObject());
                        original |= alib.checkNBTEquals(c1, (NbtCompound) stack.writeNbt(new NbtCompound()));
                    }
                }
            }
        }
        return original;
    }
}
