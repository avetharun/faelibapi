package dev.feintha.apis.bundle;

import dev.feintha.apis.faelib.FaeAPI;
import dev.feintha.apis.faelib.apis.FaeImGuiAPI;
import dev.feintha.apis.faelib.apis.FaeUnsafeAPI;
import net.fabricmc.api.ModInitializer;

public class FaeBundledAPIs implements ModInitializer {
    @Override
    public void onInitialize() {

    }
    public static enum ModloaderType {
        FABRIC, FORGE, ;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }
    public static final ModloaderType CurrentLoader = System.getProperties().keySet().stream().anyMatch(o -> ((String)o).startsWith("forge")) ? ModloaderType.FORGE : ModloaderType.FABRIC;
    final public static boolean DEBUG_ENABLED =
            Boolean.parseBoolean(System.getProperty("fabric.development", "false")) ||
                    Boolean.parseBoolean(System.getProperty("forge.development", "false")) ||
                    Boolean.parseBoolean(System.getProperty("development", "false")) ||
                    Boolean.parseBoolean(System.getProperty("mc.development", "false")) ||
                    Boolean.parseBoolean(System.getProperty("minecraft.development", "false")) ||
                    System.getProperty("javaagent", "").toLowerCase().contains("intellij") ||
                    System.getProperty("javaagent", "").toLowerCase().contains("idea") ||
                    System.getProperty("javaagent", "").toLowerCase().contains("eclipse");
    static {
        System.setProperty("java.awt.headless", "false");
        FaeAPI.registerFaeExt(new FaeUnsafeAPI());
        FaeAPI.registerFaeExt(new FaeImGuiAPI());
    }
}
