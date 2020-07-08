package dev.anhcraft.inst;

import dev.anhcraft.inst.exceptions.RuntimeError;
import dev.anhcraft.inst.lang.DataType;
import dev.anhcraft.inst.lang.FunctionLinker;
import dev.anhcraft.inst.lang.Instruction;
import dev.anhcraft.inst.values.Val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Session {
    private final VM VM;
    private final Instruction[] instructions;
    private int currentInstruction;

    Session(@NotNull VM VM, @NotNull Instruction[] instructions) {
        this.VM = VM;
        this.instructions = instructions;
    }

    @NotNull
    public VM getVM() {
        return VM;
    }

    @NotNull
    public Instruction[] getInstructions() {
        return instructions;
    }

    public void execute() {
        while (currentInstruction < instructions.length) {
            Instruction inst = instructions[currentInstruction];
            DataType[] params = new DataType[inst.getArguments().length];
            int i = 0;
            for (Val<?> v : inst.getArguments()) {
                params[i++] = v.type();
            }
            FunctionLinker f = VM.getFunction(inst.getNamespace(), inst.getFunction(), params);
            if (f == null) {
                throw new RuntimeError(String.format("Function not found (%s:%s) with parameters (%s)", inst.getNamespace(), inst.getFunction(), Arrays.stream(params).map(Enum::name).map(String::toLowerCase).collect(Collectors.joining(", "))));
            }
            if(inst.getCondition() == null || inst.getCondition().test()) {
                f.call(this, inst.getArguments());
            }
            currentInstruction++;
        }
    }

    public int getCurrentInstruction() {
        return currentInstruction;
    }

    public void setCurrentInstruction(int currentInstruction) {
        this.currentInstruction = currentInstruction;
    }
}
