package org.summer.core.bean;

import org.junit.jupiter.api.Test;
import org.summer.core.util.DependencyGraph;
import org.summer.example.ElectricPowerSwitch;
import org.summer.example.LightBulb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanDependencyGraphTest {
    @Test
    void shouldAddElement() {
        DependencyGraph<Class<?>> beanDependencyGraph = new DependencyGraph<>();
        Class<LightBulb> lightBulbClass = LightBulb.class;

        List<Class<?>> dependencies = new ArrayList<>();
        beanDependencyGraph.addElement(lightBulbClass, dependencies);
        assertEquals(1, beanDependencyGraph.topologicalSort().size());
    }

    @Test
    void shouldSortDependencies() {
        DependencyGraph<Class<?>> beanDependencyGraph = new DependencyGraph<>();

        Class<LightBulb> lightBulbClass = LightBulb.class;
        Class<ElectricPowerSwitch> electricPowerSwitchClass = ElectricPowerSwitch.class;
        List<Class<?>> dependencies = Collections.singletonList(electricPowerSwitchClass);
        Class<org.summer.example.Test> testClass = org.summer.example.Test.class;
        List<Class<?>> testDependencies = List.of(new Class<?>[]{ElectricPowerSwitch.class, LightBulb.class});

        beanDependencyGraph.addElement(testClass, testDependencies);
        beanDependencyGraph.addElement(electricPowerSwitchClass, new ArrayList<>());
        beanDependencyGraph.addElement(lightBulbClass, dependencies);

        List<Class<?>> sortedBeans = beanDependencyGraph.topologicalSort();

        assertEquals(electricPowerSwitchClass, sortedBeans.get(0));
        assertEquals(lightBulbClass, sortedBeans.get(1));
        assertEquals(testClass, sortedBeans.get(2));

    }

}