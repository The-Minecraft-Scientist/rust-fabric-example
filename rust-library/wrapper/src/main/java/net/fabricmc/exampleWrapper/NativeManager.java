package net.fabricmc.exampleWrapper;

import jdk.incubator.foreign.SymbolLookup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;


import java.io.File;

public class NativeManager {

    public static SymbolLookup nativeLookup = null;
    public static final Logger LOGGER = LogManager.getLogger("ExampleModNativeManager", new StringFormatterMessageFactory());

    public static void loadNatives() {
        if (nativeLookup != null) {
            return;
        }

        String overwrite = System.getProperty("example_mod.native_lib");
        if (overwrite != null) {
            File overwriteFile = new File(overwrite).getAbsoluteFile();
            LOGGER.info("attempting to load natives from " + overwrite);
            System.load(overwriteFile.getAbsolutePath());
            nativeLookup = SymbolLookup.loaderLookup();
            return;
        }
    }
}
