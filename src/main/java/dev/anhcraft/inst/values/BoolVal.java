package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface BoolVal extends Val<Boolean> {
    @NotNull
    default DataType type(){
        return DataType.BOOL;
    }
}
