package com.studiopulsar.feintha.faelib.interfaces;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.TypeInfo;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;

public interface FaeAPI {
    String name();
    public static void registerFaeExt(FaeExtension extension) {
        exts.add(extension);
    }
    static ArrayList<FaeExtension> exts = new ArrayList<>();
    public static ImmutableList<FaeExtension> getExtensions() {
        return ImmutableList.copyOf(exts);
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> getGenericClass() {
        TypeInformation<T> instance = new TypeInformation<T>();
        TypeVariable<?>[] parameters = instance.getClass().getTypeParameters();

        return (Class<T>)parameters[0].getClass();
    }
    // Generic helper class which (only) provides type information. This avoids the
    //   usage of a local variable of type T, which would have to be initialized.
     final class TypeInformation<T> {
        private TypeInformation() { }
    }
    public static @Nullable FaeExtension getExtension(String extName) {
        return exts.stream().filter(extension -> extension.name().equalsIgnoreCase(extName)).findFirst().orElse(null);
    }
    public static boolean doesExtensionExist(String extName) {
        return exts.stream().anyMatch(extension -> extension.name().equalsIgnoreCase(extName));
    }
    public static <E extends FaeExtension> E getExtension(Class<? extends E> extCls) {
        return (E) exts.stream().filter(extension -> extCls.isAssignableFrom(extension.getClass())).findFirst().orElse(null);
    }
    public static boolean doesExtensionExist(Class<? extends FaeExtension> extCls) {
        return exts.stream().anyMatch(extension -> extCls.isAssignableFrom(extension.getClass()));
    }
}
