package net.fabricmc.example.natives;

import jdk.incubator.foreign.SymbolLookup;
import net.fabricmc.example.ExampleMod;
import java.io.File;

public class Lib {
    public static SymbolLookup nativeLookup = null;
    public static void loadNatives() {
        if (nativeLookup != null) {
            return;
        }
        String overwrite = System.getProperty("example_mod.native_lib");
        if (overwrite != null) {
            File overwriteFile = new File(overwrite).getAbsoluteFile();
            ExampleMod.LOGGER.info("attempting to load natives from " + overwrite);
            System.load(overwriteFile.getAbsolutePath());
            nativeLookup = SymbolLookup.loaderLookup();
            return;
        }
    }
}
