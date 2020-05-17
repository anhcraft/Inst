package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

public abstract class Reference implements Val<Object> {
    private final String target;

    protected Reference(@NotNull String target) {
        this.target = target;
    }

    public abstract Val<?> getVar();

    @NotNull
    public String getTarget() {
        return target;
    }
}
