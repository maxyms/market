package com.msaddt.market.test;

import java.util.ArrayList;
import java.util.List;

public class TestSingleton {
    private static List<String> vals;
    private static int inits = 0;

    public void process() {
        List<String> vals = getVals();
        int i = 0;
        for (String val : vals) {
            for (int j = 0; j < 100000; j++) {
                int l = j + i++;
            }
            System.out.println(Thread.currentThread().getName() + ", value " + i++ + "=" + val);
        }
        //        int k = vals.size();
        //        System.out.println(Thread.currentThread().getName() + ", vals number " + vals.size());
        if (inits > 1) {
            System.out.println("Inits #=" + inits);
        }
    }

    private List<String> getVals() {
        if (vals == null) {
            synchronized (TestSingleton.class) {
                if (vals == null) {
                    inits = inits + 1;
                    vals = load();
                }
            }
        }
        return vals;
    }

    private List<String> load() {
        List<String> vals = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            vals.add("Value " + i);
        }
        return vals;
    }
}
