package com.msaddt.market.test;

import java.util.HashSet;
import java.util.Set;

public class SharedResource {
    private int counter;
    private Set<Integer> oldValues = new HashSet<Integer>();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
        if (oldValues.contains(counter)) {
            throw new RuntimeException();
        }
        oldValues.add(counter);
    }
}
