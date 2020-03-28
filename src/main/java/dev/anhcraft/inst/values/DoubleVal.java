package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import org.jetbrains.annotations.NotNull;

public interface DoubleVal extends NumberVal<Double> {
    @NotNull
    default DataType type(){
        return DataType.DOUBLE;
    }
}
