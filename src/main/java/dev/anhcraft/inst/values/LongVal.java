package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface LongVal extends NumberVal<Long> {
    @NotNull
    default DataType type(){
        return DataType.LONG;
    }
}
