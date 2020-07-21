package dev.anhcraft.inst;

import dev.anhcraft.inst.lang.Instruction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Integer> labels = new HashMap<>();
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
            if(inst.getCondition() == null || inst.getCondition().test()) {
                inst.getFunctionLinker().call(this, inst.getArguments());
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

    public Map<String, Integer> getLabels() {
        return labels;
    }
}
