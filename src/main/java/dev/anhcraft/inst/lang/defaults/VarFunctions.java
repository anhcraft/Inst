package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.lang.DataType;
import dev.anhcraft.inst.lang.Reference;
import dev.anhcraft.inst.values.BoolVal;
import dev.anhcraft.inst.values.StringVal;
import dev.anhcraft.inst.values.Val;

@Namespace("Var")
public class VarFunctions {
    @Function("Set")
    public void set(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), value);
    }

    @Function("SetInt")
    public void setInt(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), Val.of(DataType.INT, value.getData()));
    }

    @Function("SetLong")
    public void setLong(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), Val.of(DataType.LONG, value.getData()));
    }

    @Function("SetDouble")
    public void setDouble(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), Val.of(DataType.DOUBLE, value.getData()));
    }

    @Function("SetBool")
    public void setBool(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), Val.of(DataType.BOOL, value.getData()));
    }

    @Function("SetString")
    public void setString(VM vm, Reference var, Val<?> value) {
        vm.setVariable(var.getTarget(), Val.of(DataType.STRING, value.getData()));
    }

    @Function("Remove")
    public void remove(VM vm, Reference var) {
        vm.setVariable(var.getTarget(), null);
    }

    @Function("Exists")
    public void exists(VM vm, Reference resultVar, Reference var) {
        vm.setVariable(resultVar.getTarget(), new BoolVal(vm.getVariable(var.getTarget()) != null));
    }

    @Function("GetType")
    public void getType(VM vm, Reference resultVar, Reference var) {
        vm.setVariable(resultVar.getTarget(), new StringVal(var.getType().name()));
    }
}
