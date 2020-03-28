package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

public interface FunctionLinker {
    void call(@NotNull Session session, @NotNull Val<?>... args);
}
