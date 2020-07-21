package dev.anhcraft.inst;

import org.junit.Test;

import static dev.anhcraft.inst.TestUtil.exec;

public class BasicTest {
    @Test
    public void systemTest() {
        exec(new VM(), "/basic/system.txt");
    }

    @Test
    public void instTest() {
        exec(new VM(), "/basic/inst.txt");
    }

    @Test
    public void varTest() {
        exec(new VM(), "/basic/var.txt");
    }

    @Test
    public void stringTest() {
        exec(new VM(), "/basic/string.txt");
    }

    @Test
    public void mathTest() {
        exec(new VM(), "/basic/math.txt");
    }
}
