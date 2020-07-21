package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public enum ParamType {
    // specific data types
    INT(DataType.INT, null),
    LONG(DataType.LONG, null),
    DOUBLE(DataType.DOUBLE, null),
    BOOL(DataType.BOOL, null),
    STRING(DataType.STRING, null),
    // general data types
    VAL(null, Val.class),
    // non-data types
    VM(null, dev.anhcraft.inst.VM.class),
    SESSION(null, Session.class),
    REFERENCE(null, Reference.class);

    private static final Map<DataType, ParamType> TYPE_MAP = new EnumMap<>(DataType.class);

    static {
        for(ParamType paramType : ParamType.values()){
            DataType dataType = paramType.getDataType();
            if(dataType == null) continue;
            TYPE_MAP.put(dataType, paramType);
        }
    }

    @NotNull
    public static ParamType findByDataType(@NotNull DataType dataType){
        return TYPE_MAP.get(dataType);
    }

    private final DataType dataType;
    private final Class<?> paramClass;

    ParamType(@Nullable DataType dataType, @Nullable Class<?> paramClass) {
        this.dataType = dataType;
        this.paramClass = dataType != null ? dataType.getValueClass() : paramClass;
    }

    @Nullable
    public DataType getDataType() {
        return dataType;
    }

    @NotNull
    public Class<?> getParamClass() {
        return paramClass;
    }
}
