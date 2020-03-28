package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.*;
import org.jetbrains.annotations.NotNull;

@Namespace("Cache")
public class CacheFunctions {
    @Function("SetString")
    public void set(VM vm, StringVal var, StringVal value) {
        vm.setVariable(var.get(), value);
    }

    @Function("SetInt")
    public void set(VM vm, StringVal var, IntVal value) {
        vm.setVariable(var.get(), value);
    }

    @Function("SetLong")
    public void set(VM vm, StringVal var, LongVal value) {
        vm.setVariable(var.get(), value);
    }

    @Function("SetDouble")
    public void set(VM vm, StringVal var, DoubleVal value) {
        vm.setVariable(var.get(), value);
    }

    @Function("SetBool")
    public void set(VM vm, StringVal var, BoolVal value) {
        vm.setVariable(var.get(), value);
    }

    @Function("Remove")
    public void remove(VM vm, StringVal var) {
        vm.setVariable(var.get(), null);
    }

    @Function("Exists")
    public void exists(VM vm, StringVal var, StringVal resultVar) {
        vm.setVariable(resultVar.get(), new BoolVal() {
            final boolean b = vm.getVariable(var.get()) != null;

            @NotNull
            @Override
            public Boolean get() {
                return b;
            }
        });
    }

    @Function("Copy")
    public void copy(VM vm, StringVal sourceVar, StringVal targetVar) {
        vm.setVariable(targetVar.get(), vm.getVariable(sourceVar.get()));
    }
}
