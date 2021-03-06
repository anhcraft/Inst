package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public enum RelationalOp {
    GREATER(">", (a, b) -> {
        DataType dt1 = a.getType();
        DataType dt2 = b.getType();
        if(dt1.isNumber() && dt2.isNumber()) {
            Number n1 = (Number) a.getData();
            Number n2 = (Number) b.getData();
            if(dt1 == DataType.INT && dt2 == DataType.INT) return n1.intValue() > n2.intValue();
            else if(dt1 == DataType.LONG && dt2 == DataType.LONG) return n1.longValue() > n2.longValue();
            else return n1.doubleValue() > n2.doubleValue();
        }
        return false;
    }),
    GREATER_OR_EQUAL(">=", (a, b) -> {
        DataType dt1 = a.getType();
        DataType dt2 = b.getType();
        if(dt1.isNumber() && dt2.isNumber()) {
            Number n1 = (Number) a.getData();
            Number n2 = (Number) b.getData();
            if(dt1 == DataType.INT && dt2 == DataType.INT) return n1.intValue() >= n2.intValue();
            else if(dt1 == DataType.LONG && dt2 == DataType.LONG) return n1.longValue() >= n2.longValue();
            else return n1.doubleValue() >= n2.doubleValue();
        }
        return false;
    }),
    LESS("<", (a, b) -> {
        DataType dt1 = a.getType();
        DataType dt2 = b.getType();
        if(dt1.isNumber() && dt2.isNumber()) {
            Number n1 = (Number) a.getData();
            Number n2 = (Number) b.getData();
            if(dt1 == DataType.INT && dt2 == DataType.INT) return n1.intValue() < n2.intValue();
            else if(dt1 == DataType.LONG && dt2 == DataType.LONG) return n1.longValue() < n2.longValue();
            else return n1.doubleValue() < n2.doubleValue();
        }
        return false;
    }),
    LESS_OR_EQUAL("<=", (a, b) -> {
        DataType dt1 = a.getType();
        DataType dt2 = b.getType();
        if(dt1.isNumber() && dt2.isNumber()) {
            Number n1 = (Number) a.getData();
            Number n2 = (Number) b.getData();
            if(dt1 == DataType.INT && dt2 == DataType.INT) return n1.intValue() <= n2.intValue();
            else if(dt1 == DataType.LONG && dt2 == DataType.LONG) return n1.longValue() <= n2.longValue();
            else return n1.doubleValue() <= n2.doubleValue();
        }
        return false;
    }),
    EQUAL("==", (a, b) -> a.getData().equals(b.getData())),
    NOT_EQUAL("!=", (a, b) -> !a.getData().equals(b.getData()));

    private final String stringify;
    private final BiFunction<Val<?>, Val<?>, Boolean> check;

    RelationalOp(String stringify, BiFunction<Val<?>, Val<?>, Boolean> check) {
        this.check = check;
        this.stringify = stringify;
    }

    public boolean test(Val<?> a, Val<?> b) {
        return check.apply(a, b);
    }

    @NotNull
    public String toString() {
        return stringify;
    }
}
