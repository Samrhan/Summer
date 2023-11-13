package org.summer.lightbulb;

import org.summer.core.annotation.Component;

@Component
public class ElectricPowerSwitch {
    private LightBulb client;
    private boolean on;

    public ElectricPowerSwitch(LightBulb client) {
        this.client = client;
        this.on = false;
    }

    public void press() {
        if (on) {
            client.turnOff();
            on = false;
        } else {
            client.turnOn();
            on = true;
        }
    }
}
