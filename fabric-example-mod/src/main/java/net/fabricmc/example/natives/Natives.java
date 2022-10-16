package net.fabricmc.example.natives;


import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.NativeSymbol;
import net.fabricmc.example.ExampleMod;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static jdk.incubator.foreign.ValueLayout.JAVA_FLOAT;

public class Natives {
    static MethodHandle HANDLE;
    static {
        Lib.loadNatives();
        CLinker linker = CLinker.systemCLinker();

        Optional<NativeSymbol> result = Lib.nativeLookup.lookup("test_fn1");
        HANDLE = linker.downcallHandle(result.get(), FunctionDescriptor.of(JAVA_FLOAT));
    }

    public static float callRust() {
        try {
            return (float) HANDLE.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
