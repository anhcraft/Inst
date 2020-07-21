package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum DataType {
    INT(IntVal.class, new Class[]{
            Byte.class, byte.class,
            Short.class, short.class,
            Integer.class, int.class
    }, 0),
    LONG(LongVal.class, new Class[]{Long.class, long.class}, 0L),
    DOUBLE(DoubleVal.class, new Class[]{
            Float.class, float.class,
            Double.class, double.class
    }, 0d),
    BOOL(BoolVal.class, new Class[]{Boolean.class, boolean.class}, false),
    STRING(StringVal.class, new Class[]{String.class}, "");

    private static final Map<Class<?>, DataType> VALUE_CLASSES = new HashMap<>();
    private static final Map<Class<?>, DataType> DATA_CLASSES = new HashMap<>();

    static {
        for(DataType type : DataType.values()){
            VALUE_CLASSES.put(type.getValueClass(), type);
            for (Class<?> clazz : type.getDataClasses()) {
                DATA_CLASSES.put(clazz, type);
            }
        }
    }

    @Nullable
    public static DataType findByValueClass(Class<?> clazz){
        return VALUE_CLASSES.get(clazz);
    }

    @Nullable
    public static DataType findByDataClass(@NotNull Class<?> clazz){
        return DATA_CLASSES.get(clazz);
    }

    private final Class<? extends Val<?>> valueClass;
    private final Class<?>[] dataClasses;
    private final Object defaultData;

    <T> DataType(@NotNull Class<? extends Val<T>> valueClass, @NotNull Class<?>[] dataClasses, @NotNull T defaultData) {
        this.valueClass = valueClass;
        this.dataClasses = dataClasses;
        this.defaultData = defaultData;
    }

    public boolean isNumber() {
        return this == INT || this == LONG || this == DOUBLE;
    }

    @NotNull
    public Class<? extends Val<?>> getValueClass() {
        return valueClass;
    }

    @NotNull
    public Class<?>[] getDataClasses() {
        return dataClasses;
    }

    @NotNull
    public Object getDefaultData() {
        return defaultData;
    }
}
