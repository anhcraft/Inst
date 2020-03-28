package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface IntVal extends NumberVal<Integer> {
    @NotNull
    default DataType type(){
        return DataType.INT;
    }
}
