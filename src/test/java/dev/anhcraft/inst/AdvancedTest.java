package dev.anhcraft.inst;

import org.junit.Test;

import static dev.anhcraft.inst.TestUtil.exec;

public class AdvancedTest {
    @Test
    public void loopTest() {
        exec(new VM(), "/advanced/loop.txt");
    }

    @Test
    public void functionTest() {
        exec(new VM(), "/advanced/function.txt");
    }
}
