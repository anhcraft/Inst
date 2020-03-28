package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface Val<T> {
    @NotNull
    DataType type();

    @NotNull
    T get();
}
