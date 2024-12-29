package dev.feintha.apis.itemrenderingapi.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.feintha.apis.itemrenderingapi.ItemRenderingAPI;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Mixin(SimpleRegistry.class)
    public static class RegistryMixin<T> {
        @Inject(method="freeze", at=@At("TAIL"))
        void freezeMixin(CallbackInfoReturnable<Registry<T>> cir) {

        }
    }
    @WrapWithCondition(method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"))
    boolean renderModifyModelMixinGlint(ItemRenderer instance, BakedModel __model, ItemStack stack, int __light, int __overlay, MatrixStack matrices, @SuppressWarnings("ParameterCanBeLocal") VertexConsumer vertices,
                                        @Local(argsOnly = true) LocalRef<BakedModel> model,
                                        @Local(argsOnly = true, ordinal = 0) LocalIntRef light,
                                        @Local(argsOnly = true, ordinal = 1) LocalIntRef overlay,
                                        @Local(ordinal = 2) boolean bl2,
                                        @Local(argsOnly = true) VertexConsumerProvider vertexConsumers,
                                        @Local RenderLayer renderLayer,
                                        @Local(argsOnly = true) ModelTransformationMode mode
    ) {

        ItemRenderingAPI.GetModelsForStack(stack).forEach(customItemModel -> {
            matrices.push();
            customItemModel.renderItemModel(stack, matrices, vertexConsumers, light, overlay, mode);
            matrices.pop();
        });
        return ItemRenderingAPI.ShouldItemDefaultRender(stack, mode);
    }
    @WrapWithCondition(method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/BuiltinModelItemRenderer;render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"))
    boolean renderModifyModelMixinWithoutGlint(BuiltinModelItemRenderer instance, ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int __light, int __overlay,  @Local(argsOnly = true) LocalRef<BakedModel> model, @Local(argsOnly = true, ordinal = 0) LocalIntRef light, @Local(argsOnly = true, ordinal = 1) LocalIntRef overlay) {

        ItemRenderingAPI.GetModelsForStack(stack).forEach(customItemModel -> {
            matrices.push();
            customItemModel.renderItemModel(stack, matrices, vertexConsumers, light, overlay, mode);
            matrices.pop();
        });
        return ItemRenderingAPI.ShouldItemDefaultRender(stack, mode);
    }
    @Inject(method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at=@At("HEAD"))
    void renderItemMixin(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        ItemRenderingAPI.CURRENT_TRANSFORMATION_MODE = renderMode;
    }
}
