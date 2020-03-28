package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.DoubleVal;
import dev.anhcraft.inst.values.StringVal;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Namespace("Math")
public class MathFunctions {
    @Function("Round")
    public void round(VM vm, DoubleVal num, StringVal resultVar) {
        vm.setVariable(resultVar.get(), new DoubleVal() {
            @NotNull
            @Override
            public Double get() {
                return new BigDecimal(num.get().toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
        });
    }
}
