package dev.feintha.apis.itemrenderingapi.client;

import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface CustomItemModel {
    /**
     *
     * @param stack
     * @param matrices
     * @param vertexConsumers
     * @param light Reference to the "light" param for rendering items. Will effect other models and the base item rendering
     * @param overlay See: light param
     * @param transformationMode
     */
    void renderItemModel(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LocalIntRef light, LocalIntRef overlay, ModelTransformationMode transformationMode);
}
