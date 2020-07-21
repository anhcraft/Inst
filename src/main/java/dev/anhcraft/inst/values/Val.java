package dev.anhcraft.inst.values;

import dev.anhcraft.inst.lang.DataType;
import dev.anhcraft.inst.utils.ObjectUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Val<T> {
    @NotNull
    public static Val<?> of(@NotNull Object object){
        if(object instanceof Byte){
            return new IntVal(((Byte) object).intValue());
        } else if(object instanceof Short){
            return new IntVal(((Short) object).intValue());
        } else if(object instanceof Integer){
            return new IntVal((Integer) object);
        } else if(object instanceof Double){
            return new DoubleVal((Double) object);
        } else if(object instanceof Float){
            return new DoubleVal(((Float) object).doubleValue());
        } else if(object instanceof Long){
            return new LongVal((Long) object);
        } else if(object instanceof Boolean){
            return new BoolVal((Boolean) object);
        } else if(object instanceof String){
            return new StringVal((String) object);
        }
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static Val<?> of(@NotNull DataType type, @NotNull Object object){
        switch (type){
            case INT: return new IntVal(ObjectUtil.toInt(object));
            case LONG: return new LongVal(ObjectUtil.toLong(object));
            case DOUBLE: return new DoubleVal(ObjectUtil.toDouble(object));
            case BOOL: return new BoolVal(ObjectUtil.toBool(object));
            case STRING: return new StringVal(ObjectUtil.toString(object));
        }
        throw new UnsupportedOperationException();
    }

    private final T data;

    public Val(T data) {
        this.data = data;
    }

    @NotNull
    public T getData() {
        return data;
    }

    @NotNull
    public DataType getType() {
        return Objects.requireNonNull(DataType.findByValueClass(getClass()));
    }

    @NotNull
    public Val<?> duplicate() {
        return Val.of(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Val<?> val = (Val<?>) o;
        return data.equals(val.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
