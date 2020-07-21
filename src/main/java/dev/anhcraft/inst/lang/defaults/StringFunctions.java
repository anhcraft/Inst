package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.lang.Reference;
import dev.anhcraft.inst.utils.ObjectUtil;
import dev.anhcraft.inst.values.BoolVal;
import dev.anhcraft.inst.values.IntVal;
import dev.anhcraft.inst.values.StringVal;
import dev.anhcraft.inst.values.Val;

import java.util.Arrays;

@Namespace("String")
public class StringFunctions {
    @Function("Concat")
    public void concat(VM vm, Reference resultVar, Val<?>... vals){
        if(vals == null) return;
        if(vals.length == 1) {
            vm.setVariable(resultVar.getTarget(), new StringVal(ObjectUtil.toString(vals[0].getData())));
        } else if(vals.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(Val<?> v : vals){
                sb.append(ObjectUtil.toString(v.getData()));
            }
            vm.setVariable(resultVar.getTarget(), new StringVal(sb.toString()));
        }
    }

    @Function("Format")
    public void format(VM vm, Reference resultVar, StringVal format, Val<?>... vals){
        if(vals == null) return;
        vm.setVariable(resultVar.getTarget(), new StringVal(String.format(format.getData(), Arrays.stream(vals)
                .map(Val::getData)
                .toArray(Object[]::new)
        )));
    }

    @Function("Repeat")
    public void repeat(VM vm, Reference resultVar, StringVal str, IntVal times){
        int t = times.getData();
        switch (t) {
            case 0: {
                vm.setVariable(resultVar.getTarget(), new StringVal(""));
                break;
            }
            case 1: {
                vm.setVariable(resultVar.getTarget(), new StringVal(str.getData()));
                break;
            }
            default: {
                StringBuilder sb = new StringBuilder();
                String p = str.getData();
                for (int i = 0; i < t; i++) {
                    sb.append(p);
                }
                vm.setVariable(resultVar.getTarget(), new StringVal(sb.toString()));
            }
        }
    }

    @Function("Length")
    public void length(VM vm, Reference resultVar, StringVal str){
        vm.setVariable(resultVar.getTarget(), new IntVal(str.getData().length()));
    }

    @Function("LowerCase")
    public void lowerCase(VM vm, Reference resultVar, StringVal str){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().toLowerCase()));
    }

    @Function("UpperCase")
    public void upperCase(VM vm, Reference resultVar, StringVal str){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().toUpperCase()));
    }

    @Function("HasPrefix")
    public void hasPrefix(VM vm, Reference resultVar, StringVal str, StringVal prefix){
        vm.setVariable(resultVar.getTarget(), new BoolVal(str.getData().startsWith(prefix.getData())));
    }

    @Function("HasSuffix")
    public void hasSuffix(VM vm, Reference resultVar, StringVal str, StringVal suffix){
        vm.setVariable(resultVar.getTarget(), new BoolVal(str.getData().endsWith(suffix.getData())));
    }

    @Function("Trim")
    public void trim(VM vm, Reference resultVar, StringVal str){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().trim()));
    }

    @Function("Find")
    public void find(VM vm, Reference resultVar, StringVal str, StringVal sub){
        vm.setVariable(resultVar.getTarget(), new IntVal(str.getData().indexOf(sub.getData())));
    }

    @Function("Contains")
    public void contains(VM vm, Reference resultVar, StringVal str, StringVal sub){
        vm.setVariable(resultVar.getTarget(), new BoolVal(str.getData().contains(sub.getData())));
    }

    @Function("Replace")
    public void replace(VM vm, Reference resultVar, StringVal str, StringVal target, StringVal replacement){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().replace(target.getData(), replacement.getData())));
    }

    @Function("ReplaceAll")
    public void replaceAll(VM vm, Reference resultVar, StringVal str, StringVal pattern, StringVal replacement){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().replaceAll(pattern.getData(), replacement.getData())));
    }

    @Function("Substring")
    public void substring(VM vm, Reference resultVar, StringVal str, IntVal start){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().substring(start.getData())));
    }

    @Function("Substring")
    public void substring(VM vm, Reference resultVar, StringVal str, IntVal start, IntVal end){
        vm.setVariable(resultVar.getTarget(), new StringVal(str.getData().substring(start.getData(), end.getData())));
    }
}
