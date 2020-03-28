package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

public class Condition {
    private Val<?> left;
    private Val<?> right;
    private RelationalOp sign;

    public Condition(@NotNull Val<?> left, @NotNull Val<?> right, @NotNull RelationalOp sign) {
        this.left = left;
        this.right = right;
        this.sign = sign;
    }

    @NotNull
    public Val<?> getLeft() {
        return left;
    }

    @NotNull
    public Val<?> getRight() {
        return right;
    }

    @NotNull
    public RelationalOp getSign() {
        return sign;
    }

    public boolean test() {
        return sign.test(left, right);
    }
}
