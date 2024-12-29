package dev.feintha.apis.faelib;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class FaeUnsafeAPI implements FaeExtension {

    public static void setFinalStatic(Field field, Object value) throws Exception{
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);

        unsafe.putObject(fieldBase, fieldOffset, value);
    }
    public static void setFinal(Field field, Object value) throws Exception{
        Object fieldBase = unsafe.objectFieldOffset(field);
        long fieldOffset = unsafe.objectFieldOffset(field);
        unsafe.putObject(fieldBase, fieldOffset, value);
    }
    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception ex) { throw new Error(ex); }
    }

    @Override
    public String name() {
        return "unsafe";
    }
}
