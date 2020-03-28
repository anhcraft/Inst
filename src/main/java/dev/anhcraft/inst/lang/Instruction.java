package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.StringVal;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Instruction {
    private String namespace;
    private String function;
    private Val<?>[] args;
    private Condition condition;

    public Instruction(@NotNull String namespace, @NotNull String function, @NotNull Val<?>[] args) {
        this.namespace = namespace;
        this.function = function;
        this.args = args;
    }

    @NotNull
    public String getNamespace() {
        return namespace;
    }

    @NotNull
    public String getFunction() {
        return function;
    }

    @NotNull
    public Val<?>[] getArguments() {
        return args;
    }

    @Nullable
    public Condition getCondition() {
        return condition;
    }

    public void setCondition(@Nullable Condition condition) {
        this.condition = condition;
    }

    private String stringify(Val<?> v, boolean f) {
        if(v instanceof Reference) {
            return "$" + ((Reference) v).getTarget();
        } else if(v instanceof StringVal) {
            StringBuilder str = new StringBuilder();
            String s = ((StringVal) v).get();
            for (char c : s.toCharArray()){
                if(c == '$') {
                    str.append("\\$");
                } else if(c == '"') {
                    str.append("\\\"");
                } else if(f && c == ')') {
                    str.append("\\)");
                } else if(c == '\\') {
                    str.append("\\\\)");
                } else {
                    str.append(c);
                }
            }
            return "\"" + str.toString() + "\"";
        }
        return String.valueOf(v.get());
    }

    @NotNull
    public String toString() {
        return namespace + ":" + function + "(" + Arrays.stream(args).map(a -> stringify(a, true)).collect(Collectors.joining(" ")) + ")" + (condition == null ? "" : " " + stringify(condition.getLeft(), false) + " " + condition.getSign().toString() + " " + stringify(condition.getRight(), false));
    }
}
