package org.summer.example;

import org.summer.core.annotation.Component;

@Component
public class LightBulb {
    public void turnOff() {
        System.out.println("LightBulb: Bulb turned off...");
    }

    public void turnOn() {
        System.out.println("LightBulb: Bulb turned on...");
    }
}
