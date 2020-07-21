package dev.anhcraft.inst;

import dev.anhcraft.inst.exceptions.InstructionCompileFailed;
import dev.anhcraft.inst.lang.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TestUtil {
    public static void newSession(VM vm, String[] script){
        try {
            Session s = vm.newSession(Arrays.stream(script).map(vm::compileInstruction).toArray(Instruction[]::new));
            System.out.println("=====================================================================");
            int i = 1;
            for(Instruction inst : s.getInstructions()){
                System.out.println((i++) + ". " + inst.toString());
            }
            System.out.println("---------------------------------------------------------------------");
            long start = System.currentTimeMillis();
            s.execute();
            long delta = System.currentTimeMillis() - start;
            System.out.println("---------------------------------------------------------------------");
            System.out.println("> Script executed in "+delta+" ms");
            System.out.println("=====================================================================");
        } catch (InstructionCompileFailed instructionCompileFailed) {
            instructionCompileFailed.printStackTrace();
        }
    }

    public static void exec(VM vm, String path){
        try {
            InputStream in = BasicTest.class.getResourceAsStream(path);
            String[] script = new BufferedReader(new InputStreamReader(in))
                    .lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && !s.startsWith("//"))
                    .toArray(String[]::new);
            newSession(vm, script);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
