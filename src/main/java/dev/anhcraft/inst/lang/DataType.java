package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.values.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum DataType {
    VM,
    SESSION,
    INT,
    LONG,
    DOUBLE,
    BOOL,
    STRING;

    public boolean isNumber() {
        return this == INT || this == LONG || this == DOUBLE;
    }

    @Nullable
    public static DataType fromClass(@NotNull Class<?> clazz){
        if (clazz.isAssignableFrom(VM.class)) {
            return DataType.VM;
        } else if (clazz.isAssignableFrom(Session.class)) {
            return DataType.SESSION;
        } else if (clazz.isAssignableFrom(IntVal.class)) {
            return DataType.INT;
        } else if (clazz.isAssignableFrom(LongVal.class)) {
            return DataType.LONG;
        } else if (clazz.isAssignableFrom(DoubleVal.class)) {
            return DataType.DOUBLE;
        } else if (clazz.isAssignableFrom(BoolVal.class)) {
            return DataType.BOOL;
        } else if (clazz.isAssignableFrom(StringVal.class)) {
            return DataType.STRING;
        }
        return null;
    }
}
