package com.msaddt.market.test;

public class TestMain {
    public static void main(String... args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("---------- " + i);
            doRun();
        }
    }

    private static void doRun() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                new TestSingleton().process();
            }
        };
        for (int i = 0; i < 20; i++) {
            Thread t = new Thread(r);
            t.setName("Thread #" + (i + 1));
            t.start();
        }
    }
}
