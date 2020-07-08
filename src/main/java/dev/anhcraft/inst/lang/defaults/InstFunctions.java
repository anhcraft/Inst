package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.IntVal;

@Namespace("Inst")
public class InstFunctions {
    @Function("Jump")
    public void jumpInst(Session session, IntVal count){
        int i = count.get();
        if(i < 0) {
            throw new IllegalArgumentException("`count` must not be negative");
        }
        session.setCurrentInstruction(session.getCurrentInstruction() + i);
    }

    @Function("Return")
    public void returnInst(Session session){
        session.setCurrentInstruction(session.getInstructions().length);
    }
}
