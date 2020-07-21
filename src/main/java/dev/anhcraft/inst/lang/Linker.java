package dev.anhcraft.inst.lang;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Linker {
    private final List<ParamType> paramTypes;
    private final boolean varArgs;

    public Linker(List<ParamType> paramTypes, boolean varArgs) {
        this.paramTypes = paramTypes;
        this.varArgs = varArgs;
    }

    public abstract void call(@NotNull Session session, @NotNull List<Val<?>> args);

    @NotNull
    public List<ParamType> getParamTypes() {
        return paramTypes;
    }

    public boolean isVarArgs() {
        return varArgs;
    }

    public boolean canLink(@NotNull List<Val<?>> values){
        ParamType lastValType = null;
        int j = 0;
        for (ParamType paramType : paramTypes) {
            if (j >= values.size()) {
                break;
            }
            if (paramType == ParamType.VAL) {
                lastValType = paramType;
                j++;
            } else if (paramType == ParamType.REFERENCE) {
                Val<?> v = values.get(j);
                if (v instanceof Reference) {
                    lastValType = paramType;
                    j++;
                } else {
                    return false;
                }
            } else if (paramType.getDataType() != null) {
                Val<?> v = values.get(j);
                if (v instanceof Reference || v.getType() == paramType.getDataType()) {
                    lastValType = paramType;
                    j++;
                } else {
                    return false;
                }
            }
        }
        if(varArgs) {
            if (lastValType != null && lastValType != ParamType.VAL && lastValType != ParamType.REFERENCE) {
                while (j < values.size()) {
                    if (!values.get(j).getType().equals(lastValType.getDataType())) {
                        return false;
                    }
                    j++;
                }
            }
            return true;
        }
        return j == values.size();
    }
}
