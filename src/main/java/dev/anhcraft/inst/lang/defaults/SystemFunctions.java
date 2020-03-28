package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.LongVal;
import dev.anhcraft.inst.values.StringVal;
import org.jetbrains.annotations.NotNull;

@Namespace("System")
public class SystemFunctions {
    @Function("Print")
    public void print(StringVal str){
        System.out.print(str.get());
    }

    @Function("Newline")
    public void newline(){
        System.out.println();
    }

    @Function("Println")
    public void println(StringVal str){
        System.out.println(str.get());
    }

    @Function("GetMilliTime")
    public void getMilliTime(VM vm, StringVal resultVar){
        vm.setVariable(resultVar.get(), new LongVal() {
            final long time = System.currentTimeMillis();

            @NotNull
            @Override
            public Long get() {
                return time;
            }
        });
    }

    @Function("GetNanoTime")
    public void getNanoTime(VM vm, StringVal resultVar){
        vm.setVariable(resultVar.get(), new LongVal() {
            final long time = System.nanoTime();

            @NotNull
            @Override
            public Long get() {
                return time;
            }
        });
    }
}
