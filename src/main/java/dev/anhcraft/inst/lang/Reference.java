package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Reference extends Val<Object> {
    private final String target;

    protected Reference(@NotNull String target) {
        super(null);
        this.target = target;
    }

    public abstract Val<?> getVar();

    @NotNull
    public Object getData() {
        return getVar().getData();
    }

    @NotNull
    public DataType getType() {
        return getVar().getType();
    }

    @NotNull
    public String getTarget() {
        return target;
    }

    @NotNull
    @Deprecated
    public Val<?> duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference reference = (Reference) o;
        return target.equals(reference.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target);
    }
}
