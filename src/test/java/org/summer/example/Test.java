package org.summer.example;

import org.summer.core.annotation.Component;

@Component
public class Test {
    public Test(ElectricPowerSwitch client){
        client.press();
        client.press();
    }
}
