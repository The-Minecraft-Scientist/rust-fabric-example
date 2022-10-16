package net.fabricmc.example.natives;

import jdk.incubator.foreign.SymbolLookup;
import net.fabricmc.example.ExampleMod;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;

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
        File nativeFile = extractNatives(new File(System.getProperty("user.dir"), "examplemod" + File.separator + "natives"));
        System.load(nativeFile.getAbsolutePath());
        nativeLookup = SymbolLookup.loaderLookup();
    }
    private static File extractNatives(File dstDirectory) {
        if (!dstDirectory.exists()) {
            if (!dstDirectory.mkdirs()) {
                throw new RuntimeException("Failed to make natives directory");
            }
        }
        if (!dstDirectory.isDirectory()) {
            throw new RuntimeException("Natives directory is not a directory");
        }

        String fileName = System.mapLibraryName("rust-library");
        File nativesFile = new File(dstDirectory, fileName);
        ExampleMod.LOGGER.info("Extracting natives to " + nativesFile);

        if(nativesFile.isFile()) {
            if (!nativesFile.delete()) {
                throw new RuntimeException("Failed to delete already existing natives file");
            }
        } else {
            if (nativesFile.exists()) {
                throw new RuntimeException("Natives file already exists but is not a file");
            }
        }

        copyToFile(nativesFile, Os.getOs().name + "/" + Arch.getArch().name + "/" + fileName);

        return nativesFile;
    }
    private static void copyToFile(File dst, String resourcePath) {
        try (InputStream in = Lib.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new RuntimeException("Invalid native lib resource path: " + resourcePath);
            }

            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buffer = new byte[1024 * 1024 * 16];
                int readBytes;

                while((readBytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, readBytes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract native lib.", e);
        }
    }
    private enum Os {
        WINDOWS("windows"),
        GENERIC_LINUX("generic_linux"),
        MAC("darwin");

        public final String name;

        Os(String name) {
            this.name = name;
        }

        static Os getOs() {
            if (SystemUtils.IS_OS_WINDOWS) {
                return WINDOWS;
            } else if(SystemUtils.IS_OS_LINUX) {
                return GENERIC_LINUX;
            } else if(SystemUtils.IS_OS_MAC) {
                return MAC;
            } else {
                throw new UnsupportedOperationException("Unknown os: " + SystemUtils.OS_NAME);
            }
        }
    }
    private enum Arch {
        I686("i686"),
        AMD64("x86_64"),
        AARCH64("aarch64");

        public final String name;

        Arch(String name) {
            this.name = name;
        }

        static Arch getArch() {
            return switch (SystemUtils.OS_ARCH) {
                case "x86" -> I686;
                case "amd64" -> AMD64;
                case "x86_64" -> AMD64;
                case "aarch64" -> AARCH64;
                default -> throw new UnsupportedOperationException("Unknown arch: " + SystemUtils.OS_ARCH);
            };
        }
    }
}
