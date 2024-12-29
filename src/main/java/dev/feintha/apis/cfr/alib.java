package dev.feintha.cfr;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.joml.Vector2i;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class alib {
    public static <K, V> void MapDown(Map<K, V> map, BiConsumer<K, V> consumer) {
        map.forEach(consumer);
    }
    public static <T> RegistryWrapper.WrapperLookup getWrapperLookupForRegistry(Registry<T> registry) {
        return RegistryWrapper.WrapperLookup.of(Stream.ofNullable(registry.getTagCreatingWrapper()));
    }
    // Class to automatically create an Entity Part (Similar to how an Ender Dragon does it)
    public static abstract class EntityPart<T extends Entity> extends Entity {
        public EntityPart(EntityType<?> type, T parent, World world) {
            super(type, world);
            this.parent = parent;
        }
        public T parent;
        @Override
        public boolean isPartOf(Entity entity) {
            return entity == this || entity == parent;
        }

        @MustBeInvokedByOverriders
        @Override
        public void tick() {
            super.tick();
            if (this.parent == null) {this.discard();}
        }
    }

    public static boolean IsAdjacentBlockOf(World world, BlockPos pos, TagKey<Block> blockTagKey) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isIn(blockTagKey)) {
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static boolean IsAdjacentBlockOf(World world, BlockPos pos, Block block) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isOf(block)) {
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithProperty(World world, BlockPos pos, TagKey<Block> blockTagKey, T propertyValue, Property<T> tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isIn(blockTagKey) && world.getBlockState(blockPos).contains(tProperty) && world.getBlockState(blockPos).get(tProperty) == propertyValue) {
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithProperty(World world, BlockPos pos, Block block, T propertyValue, Property<T> tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isOf(block) && world.getBlockState(blockPos).contains(tProperty) && world.getBlockState(blockPos).get(tProperty) == propertyValue) {
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithOptionalProperty(World world, BlockPos pos, TagKey<Block> blockTagKey, T propertyValue, Property<T> tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isIn(blockTagKey)) {
                if (world.getBlockState(blockPos).contains(tProperty) && world.getBlockState(blockPos).get(tProperty) != propertyValue) {
                    return;
                }
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithOptionalProperty(World world, BlockPos pos, Block block, T propertyValue, Property<T> tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isOf(block)) {
                if (world.getBlockState(blockPos).contains(tProperty) && world.getBlockState(blockPos).get(tProperty) != propertyValue) {
                    return;
                }
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    @SafeVarargs
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithOptionalProperty(World world, BlockPos pos, TagKey<Block> blockTagKey, Pair<Property<T>, T> ... tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isIn(blockTagKey)) {
                for (Pair<Property<T>, T> pair : tProperty) {
                    if (world.getBlockState(blockPos).contains(pair.getLeft()) && world.getBlockState(blockPos).get(pair.getLeft()) != pair.getRight()) {
                        return;
                    }
                }
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    @SafeVarargs
    public static <T extends Comparable<T>>boolean IsAdjacentBlockOfAndWithOptionalProperty(World world, BlockPos pos, Block block, Pair<Property<T>, T> ... tProperty) {
        BlockPos start = pos.add(-1, -1, -1);
        BlockPos end = pos.add(1, 1, 1);
        AtomicBoolean bl1 = new AtomicBoolean(false);
        BlockPos.iterate(start, end).forEach(blockPos -> {
            if (world.getBlockState(blockPos).isOf(block)) {
                for (Pair<Property<T>, T> pair : tProperty) {
                    if (world.getBlockState(blockPos).contains(pair.getLeft()) && world.getBlockState(blockPos).get(pair.getLeft()) != pair.getRight()) {
                        return;
                    }
                }
                bl1.set(true);
            }
        });
        return bl1.get();
    }
    public static Set<BlockState> getStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }
    public static boolean playerHasItem(ServerPlayerEntity player, ItemStack targetItem) {
        for (ItemStack itemStack : player.getInventory().main) {
            // Check if the item matches the target item type (ignoring NBT)
            if (itemStack.getItem() == targetItem.getItem() && itemStack.getItem() != Items.AIR) {
                return true;
            }
        }
        return false;
    }
    public static <F,T> F getMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setPrivateMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.setAccessible(true);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F getPrivateMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getDeclaredField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkNbtEqualsElement(NbtElement left, NbtElement right) {
        return left.asString().equals(right.asString());
    }
    // Checks if left is present in and values equal in right.
    public static boolean checkNBTEquals(NbtCompound left, NbtCompound right) {
        if (left.getSize() == right.getSize() && left.getSize() == 0) {return true;}
        boolean bl = true;
        for (String key : left.getKeys()) {
            if (!bl || !right.contains(key)) {return false;}
            NbtElement e_L = left.get(key);
            NbtElement e_R = right.get(key);
            int elem_t = left.getType(key);
            if (right.getType(key) != elem_t) {
                return false;
            }
            if (left.get(key) instanceof NbtIntArray lL && right.get(key) instanceof NbtIntArray rL) {
                if (lL.size() != rL.size()) {return false;}
                int i = 0;
                for (int e : lL.getIntArray()) {
                    if (e != rL.getIntArray()[i]){
                        return false;
                    }
                    i++;
                }
                continue;
            }

            if (left.get(key) instanceof NbtByteArray lL && right.get(key) instanceof NbtByteArray rL) {
                if (lL.size() != rL.size()) {return false;}
                int i = 0;
                for (byte e : lL.getByteArray()) {
                    if (e != rL.getByteArray()[i]){
                        return false;
                    }
                    i++;
                }
                continue;
            }
            if (left.get(key) instanceof NbtLongArray lL && right.get(key) instanceof NbtLongArray rL) {
                if (lL.size() != rL.size()) {return false;}
                int i = 0;
                for (long e : lL.getLongArray()) {
                    if (e != rL.getLongArray()[i]){
                        return false;
                    }
                    i++;
                }
                continue;
            }
            if (left.get(key) instanceof NbtList lL && right.get(key) instanceof NbtList rL) {
                if (lL.size() != rL.size()) {return false;}
                int i = 0;
                for (NbtElement e : lL.subList(0, lL.size())) {
                    if (!e.asString().contentEquals(rL.get(i).asString())){
                        return false;
                    }
                    i++;
                }
                continue;
            }

            if (e_L instanceof AbstractNbtNumber numL && e_R instanceof AbstractNbtNumber numR) switch (elem_t) {
                    case NbtElement.INT_TYPE -> bl = numL.intValue() == numR.intValue();
                    case NbtElement.SHORT_TYPE -> bl = numL.shortValue() == numR.shortValue();
                    case NbtElement.BYTE_TYPE -> bl = numL.byteValue() == numR.byteValue();
                    case NbtElement.FLOAT_TYPE -> bl = numL.floatValue() == numR.floatValue();
                    case NbtElement.DOUBLE_TYPE -> bl = numL.doubleValue() == numR.doubleValue();
                    case NbtElement.LONG_TYPE -> bl = numL.longValue() == numR.longValue();
                }
            else switch (elem_t) {
                case NbtElement.COMPOUND_TYPE -> bl = checkNBTEquals(left.getCompound(key), right.getCompound(key));
                case NbtElement.STRING_TYPE -> bl = e_L.asString().contentEquals(e_R.asString());
            }
        }
        return bl;
    }

    public static NbtCompound json2NBT(JsonObject jsonObject) {
        return NbtCompound.CODEC.parse(JsonOps.INSTANCE, jsonObject).result().orElse(new NbtCompound());
    }
    public static JsonObject NBT2json(NbtCompound nbtCompound) {
        return (JsonObject) NbtCompound.CODEC.encodeStart(JsonOps.INSTANCE, nbtCompound).result().orElse(new JsonObject());
    }

    public static <T> void runMixinMethod(T mixinType, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            Method f;
            if (args.length == 0) {
                f = mixinType.getClass().getDeclaredMethod(methodName);
                f.invoke(mixinType);
            } else {
                f = mixinType.getClass().getDeclaredMethod(methodName, argTypes);
                f.invoke(mixinType, args);
            }
            if (args.length == 0) {
                f.invoke(mixinType);
                return;
            }
            f.invoke(mixinType, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> void runPrivateMixinMethod(T mixinType, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            Method f;
            if (args.length == 0) {
                f = mixinType.getClass().getDeclaredMethod(methodName);
                f.setAccessible(true);
                f.invoke(mixinType);
            } else {
                f = mixinType.getClass().getDeclaredMethod(methodName, argTypes);
                f.setAccessible(true);
                f.invoke(mixinType, args);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static float getRandomFloat(Random random, float minValue, float maxValue) {
        return minValue + random.nextFloat() * (maxValue - minValue);
    }
    public static float getRandomFloat(float minValue, float maxValue) {
        return minValue + Random.create().nextFloat() * (maxValue - minValue);
    }
    public static <T extends Entity> List<T> getEntitiesOfTypeInRange(World world, BlockPos pos, double range, EntityType<T> type) {
        Predicate<Entity> filter = entity -> entity.getType() == type && entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= range * range;
        return world.getEntitiesByType(type, Box.of(Vec3d.of(pos),range, range, range), filter);
    }
    public static boolean isEntityNearBlock(Entity e, int radius, Block... blocks) {
        // Get the entity's position
        BlockPos entityPos = e.getBlockPos();
        // Check if any nearby block is a target block type
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = entityPos.add(x, y, z);
                    Block block = e.getWorld().getBlockState(pos).getBlock();
                    for (Block b : blocks) {
                        if (block == b) {
                            return true;
                        }
                    }
                }
            }
        }

        // No nearby block is a target block type
        return false;
    }
    /**
     * Calculate a 64 bits hash by combining CRC32 with Adler32.
     *
     * @param bytes a byte array
     * @return a hash number
     */
    public static long getHash64(byte[] bytes) {

        CRC32 crc32 = new CRC32();
        Adler32 adl32 = new Adler32();

        crc32.update(bytes);
        adl32.update(bytes);

        long crc = crc32.getValue();
        long adl = adl32.getValue();
        return ((crc << 32) | adl) + crc << 8;
    }
    public static long getHash64(String s) {
        return getHash64(s.getBytes(StandardCharsets.UTF_8));
    }
    public static long bitenable(long var, long nbit) {
        return (var) |= (1L <<(nbit));
    }
    public static long bitdisable(long var, long nbit) {
        return (var) &= (1L <<(nbit));
    }
    public static long bitflip(long var, long nbit) {
        return (var) ^= (1L <<(nbit));
    }
    public static boolean getbit(long var, long nbit) {
        return ((var) & (1L <<(nbit))) == 1;
    }
    public static long setbit(long var, long nbit, boolean value) {
        if (value) {
            return bitenable(var, nbit);
        } else {
            return bitdisable(var,nbit);
        }
    }
    public static boolean isItemInHotbar(ItemStack itemStack) {
        Entity holder = itemStack.getHolder();
        if (holder instanceof PlayerEntity player) {
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 9; i++) { // iterate through the hotbar slots (0-8)
                if (inventory.getStack(i).itemMatches(itemStack.getRegistryEntry())) {
                    // the item is in the selected slot
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isBlockIn(BlockState source, TagKey<Block> tag) {
        return source.isIn(tag);
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTag(TagKey<Block> tag) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
        }));
        return data;
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTagAnd(TagKey<Block> tag, Consumer<Pair<Identifier, Block>> onFound) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
            onFound.accept(Pair.of(id,block));
        }));
        return data;
    }
    public static BlockPos getBlockPosFromArray(long[] a) {
        if (a.length < 3) {return BlockPos.ORIGIN;}
        return new BlockPos((int)a[0], (int)a[1], (int)a[2]);
    }
    public static long[] getBlockPosAsArray(BlockPos d) {
        if (d == null) {return new long[]{0, 0, 0};}
        return new long[]{d.getX(), d.getY(), d.getZ()};
    }
    public static Vector2i XYPosFromOffset(int w, int offset) {
        if (w == 0 || offset == 0) {return new Vector2i(0,0);}
        int x = offset % w;
        int y = offset / w;
        return new Vector2i(x,y);
    }
    public static double lerp(double a, double b, float f) {
        return a + f * (b - a);
    }
    public static int mixRGBA (int a, int b, float ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        float iRatio = 1.0f - ratio;

        int aA = (a >> 24 & 0xff);
        int aR = ((a & 0xff0000) >> 16);
        int aG = ((a & 0xff00) >> 8);
        int aB = (a & 0xff);

        int bA = (b >> 24 & 0xff);
        int bR = ((b & 0xff0000) >> 16);
        int bG = ((b & 0xff00) >> 8);
        int bB = (b & 0xff);

        int A = (int)((aA * iRatio) + (bA * ratio));
        int R = (int)((aR * iRatio) + (bR * ratio));
        int G = (int)((aG * iRatio) + (bG * ratio));
        int B = (int)((aB * iRatio) + (bB * ratio));

        return A << 24 | R << 16 | G << 8 | B;
    }
}
