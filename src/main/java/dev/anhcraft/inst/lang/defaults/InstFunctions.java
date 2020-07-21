package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.Session;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.lang.Instruction;
import dev.anhcraft.inst.lang.Reference;
import dev.anhcraft.inst.values.IntVal;
import dev.anhcraft.inst.values.StringVal;

@Namespace("Inst")
public class InstFunctions {
    @Function("Jump")
    public void jumpInst(Session session, IntVal count){
        session.setCurrentInstruction(session.getCurrentInstruction() + count.getData());
    }

    @Function("Return")
    public void returnInst(Session session){
        session.setCurrentInstruction(session.getInstructions().length);
    }

    @Function("Stringify")
    public void stringifyInst(Session session, Reference resultVar, IntVal offset){
        Instruction inst = session.getInstructions()[session.getCurrentInstruction()+offset.getData()];
        session.getVM().setVariable(resultVar.getTarget(), new StringVal(inst.toString()));
    }

    @Function("GetIndex")
    public void getIndex(Session session, Reference resultVar){
        session.getVM().setVariable(resultVar.getTarget(), new IntVal(session.getCurrentInstruction()));
    }

    @Function("Label")
    public void label(Session session, Reference label, IntVal offset){
        session.getLabels().put(label.getTarget(), session.getCurrentInstruction()+offset.getData()+1);
    }

    @Function("Goto")
    public void goTo(Session session, Reference label){
        session.setCurrentInstruction(session.getLabels().getOrDefault(label.getTarget(), 0) - 1);
    }
}
