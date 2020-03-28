package dev.anhcraft.inst;

import dev.anhcraft.inst.exceptions.FunctionRegisterFailed;
import dev.anhcraft.inst.exceptions.InstructionCompileFailed;
import dev.anhcraft.inst.lang.Instruction;
import dev.anhcraft.inst.values.StringVal;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;

public class InstTest {
    @Test
    public void test() {
        String[] scripts = new String[] {
                "CustomFunc:Hello()",
                "Cache:Exists($a res)",
                "CustomFunc:Say(\"Key exist!\") ? $res == true",
                "CustomFunc:Say(\"Key not exist!\") ? $res == false",
                "Cache:SetInt(num 3)",
                "CustomFunc:Say(Yoo $num)",
                "Cache:Exists(num res)",
                "CustomFunc:Say(\"Key exist!\") ? $res == true",
                "CustomFunc:Say(\"Key not exist!\") ? $res == false",
                "Cache:Copy(num num1)",
                "Cache:Exists(num1 res)",
                "CustomFunc:Say(\"Key exist!\") ? $res == true",
                "CustomFunc:Say(\"Key not exist!\") ? $res == false",
                "Cache:Remove(num1)",
                "Cache:Exists(num1 res)",
                "CustomFunc:Say(\"Key exist!\") ? $res == true",
                "CustomFunc:Say(\"Key not exist!\") ? $res == false",
                "CustomFunc:Say(hello) ? \"3\"!=\"1\"",
                "System:Print(ye)",
                "System:Println(ah)",
                "Cache:SetString(a \"The truth is true.\")",
                "Cache:SetString(b \"The lie is false\")",
                "String:Concat($a $b c)",
                "System:Println($c)",
                "String:Repeat(* 5 c)",
                "System:Println($c)",
        };
        VM vm = new VM();
        try {
            vm.registerFunctions(CustomFunctions.class);
        } catch (FunctionRegisterFailed functionRegisterFailed) {
            functionRegisterFailed.printStackTrace();
        }
        vm.setVariable("a", new StringVal() {
            @NotNull
            @Override
            public String get() {
                return "key";
            }
        });
        try {
            Session s = vm.newSession(Arrays.stream(scripts).map(vm::compileInstruction).toArray(Instruction[]::new));
            System.out.println("---------------------------------------------------------------------");
            for(Instruction i : s.getInstructions()){
                System.out.println(i.toString());
            }
            System.out.println("---------------------------------------------------------------------");
            long start = System.currentTimeMillis();
            s.execute();
            long delta = System.currentTimeMillis() - start;
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Script executed in "+delta+" ms");
        } catch (InstructionCompileFailed instructionCompileFailed) {
            instructionCompileFailed.printStackTrace();
        }
    }
}
