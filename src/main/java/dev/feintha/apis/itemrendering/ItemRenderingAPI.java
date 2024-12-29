package dev.feintha.apis.itemrenderingapi;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Function3;
import dev.feintha.apis.itemrenderingapi.client.CustomItemModel;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ItemRenderingAPI implements ClientModInitializer {

    public static ModelTransformationMode CURRENT_TRANSFORMATION_MODE = ModelTransformationMode.NONE;
    static final HashMap<Item, List<DefaultRenderPredicate>> RENDER_SKIP_TARGETS = new HashMap<>();
    static final HashMap<Item, List<Pair<Predicate<ItemStack>, CustomItemModel>>> CUSTOM_ITEM_MODELS = new HashMap<>();
    public static CustomItemModel registerItemModel(Item item, CustomItemModel model) {
        var ref = CUSTOM_ITEM_MODELS.getOrDefault(item, new ArrayList<>());
        ref.add(new Pair<>(itemStack -> true, model));
        CUSTOM_ITEM_MODELS.put(item , ref);
        return model;
    }
    public interface DefaultRenderPredicate extends Function3<Boolean, ItemStack, ModelTransformationMode, Boolean> {
        @Override
        Boolean apply(Boolean stackEmpty, ItemStack stack, ModelTransformationMode mode);
    }
    // Returns true if the item should render the base item model (ie, from resource pack)
    public static boolean ShouldItemDefaultRender(ItemStack stack, ModelTransformationMode mode) {
        return RENDER_SKIP_TARGETS.getOrDefault(stack.getItem(), new ArrayList<>()).stream().noneMatch(stackPredicate -> stackPredicate.apply(stack.isEmpty(), stack, mode));
    }
    public static ImmutableList<CustomItemModel> GetModelsForStack(ItemStack stack) {
        return ImmutableList.copyOf(CUSTOM_ITEM_MODELS.getOrDefault(stack.getItem(), ImmutableList.of()).stream().filter(predicateCustomItemModelPair -> predicateCustomItemModelPair.getLeft().test(stack)).map(Pair::getRight).toList());
    }

    /**
     * @param stackPredicate <b>Return true from predicate to skip default item rendering.</b>
     */
    public static void registerRenderSkipTarget(Item item, DefaultRenderPredicate stackPredicate) {
        var ref = RENDER_SKIP_TARGETS.getOrDefault(item, new ArrayList<>());
        ref.add(stackPredicate);
        RENDER_SKIP_TARGETS.put(item, ref);
    }
    /**
     * @param stackPredicate <b>Return true from predicate to skip default item rendering.</b>
     */
    public static void registerRenderSkipTarget(Item item, Predicate<ItemStack> stackPredicate) {
        var ref = RENDER_SKIP_TARGETS.getOrDefault(item, new ArrayList<>());
        ref.add((aBoolean, itemStack, modelTransformationMode) -> stackPredicate.test(itemStack));
        RENDER_SKIP_TARGETS.put(item, ref);
    }
    // Skip rendering this item regardless of information provided

    public static void registerRenderSkipTarget(Item item) {
        var ref = RENDER_SKIP_TARGETS.getOrDefault(item, new ArrayList<>());
        ref.add((aBoolean, itemStack, modelTransformationMode) -> true);
        RENDER_SKIP_TARGETS.put(item, ref);
    }
    public static CustomItemModel registerItemModel(Item item, CustomItemModel model, Predicate<ItemStack> predicate) {
        var ref = CUSTOM_ITEM_MODELS.getOrDefault(item, new ArrayList<>());
        ref.add(new Pair<>(predicate, model));
        CUSTOM_ITEM_MODELS.put(item , ref);
        return model;
    }
    @Override
    public void onInitializeClient() {
    }
}
