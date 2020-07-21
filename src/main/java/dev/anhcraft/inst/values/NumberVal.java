package dev.anhcraft.inst.values;

public abstract class NumberVal<T extends Number> extends Val<T> {
    public NumberVal(T data) {
        super(data);
    }
}
