package dev.anhcraft.inst;

import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.IntVal;
import dev.anhcraft.inst.values.StringVal;

@Namespace("CustomFunc")
public class CustomFunctions {
    @Function("Hello")
    public void hello(){
        System.out.println("hello");
    }

    @Function("Say")
    public void say(StringVal msg){
        System.out.println(msg.get());
    }

    @Function("Say")
    public void say(VM vm, StringVal msg, IntVal repeat){
        for (int i = 0; i < repeat.get(); i++) {
            System.out.println(msg.get());
        }
    }
}
