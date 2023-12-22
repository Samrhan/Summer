package org.summer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

        @Test
        void shouldInstantiateTestClass(){
            var output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));
            Main.main(new String[]{"org.summer.example"});

            String allWrittenLines = output.toString();
            assertTrue(allWrittenLines.contains("LightBulb: Bulb turned on..."));
            assertTrue(allWrittenLines.contains("LightBulb: Bulb turned off..."));
        }

}