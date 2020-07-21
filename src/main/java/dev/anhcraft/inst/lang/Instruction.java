package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.values.StringVal;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class Instruction {
    private final String namespace;
    private final String function;
    private final List<Val<?>> args;
    private final Linker functionLinker;
    private Condition condition;

    public Instruction(@NotNull String namespace, @NotNull String function, @NotNull List<Val<?>> args, @NotNull Linker functionLinker) {
        this.namespace = namespace;
        this.function = function;
        this.args = args;
        this.functionLinker = functionLinker;
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
    public List<Val<?>> getArguments() {
        return args;
    }

    @Nullable
    public Condition getCondition() {
        return condition;
    }

    public void setCondition(@Nullable Condition condition) {
        this.condition = condition;
    }

    @NotNull
    public Linker getFunctionLinker() {
        return functionLinker;
    }

    private String stringify(Val<?> v, boolean f) {
        if(v instanceof Reference) {
            return "$" + ((Reference) v).getTarget();
        } else if(v instanceof StringVal) {
            StringBuilder str = new StringBuilder();
            String s = ((StringVal) v).getData();
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
        return String.valueOf(v.getData());
    }

    @NotNull
    public String toString() {
        return namespace + ":" + function + "(" + args.stream().map(a -> stringify(a, true)).collect(Collectors.joining(" ")) + ")" + (condition == null ? "" : " ? " + stringify(condition.getLeft(), false) + " " + condition.getSign().toString() + " " + stringify(condition.getRight(), false));
    }
}
