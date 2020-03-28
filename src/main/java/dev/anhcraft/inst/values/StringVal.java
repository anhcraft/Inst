package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface StringVal extends Val<String> {
    @NotNull
    default DataType type(){
        return DataType.STRING;
    }
}
