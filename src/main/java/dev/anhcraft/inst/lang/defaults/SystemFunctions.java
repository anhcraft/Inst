package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.lang.Reference;
import dev.anhcraft.inst.utils.ObjectUtil;
import dev.anhcraft.inst.values.LongVal;
import dev.anhcraft.inst.values.StringVal;
import dev.anhcraft.inst.values.Val;

import java.text.SimpleDateFormat;
import java.util.Date;

@Namespace("System")
public class SystemFunctions {
    @Function("Print")
    public void print(Val<?>... vals){
        if(vals == null) return;
        if(vals.length == 1) {
            System.out.print(ObjectUtil.toString(vals[0].getData()));
        } else if(vals.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(Val<?> v : vals){
                sb.append(ObjectUtil.toString(v.getData()));
            }
            System.out.print(sb.toString());
        }
    }

    @Function("Println")
    public void println(Val<?>... vals){
        if(vals == null) return;
        if(vals.length == 1) {
            System.out.println(ObjectUtil.toString(vals[0].getData()));
        } else if(vals.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(Val<?> v : vals){
                sb.append(ObjectUtil.toString(v.getData()));
            }
            System.out.println(sb.toString());
        }
    }

    @Function("GetMilliTime")
    public void getMilliTime(VM vm, Reference resultVar){
        vm.setVariable(resultVar.getTarget(), new LongVal(System.currentTimeMillis()));
    }

    @Function("GetNanoTime")
    public void getNanoTime(VM vm, Reference resultVar){
        vm.setVariable(resultVar.getTarget(), new LongVal(System.nanoTime()));
    }

    @Function("GetDate")
    public void getDate(VM vm, Reference resultVar, StringVal format){
        vm.setVariable(resultVar.getTarget(), new StringVal(new SimpleDateFormat(format.getData()).format(new Date())));
    }
}
