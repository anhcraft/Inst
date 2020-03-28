package dev.anhcraft.inst.exceptions;

public class InstructionCompileFailed extends RuntimeException {
    public InstructionCompileFailed(String msg) {
        super(msg);
    }
}
