package com.msaddt.market.test;

public class TestSharedResource {
    private static final SharedResource resource = new SharedResource();
    private static Integer threadsDone = 0;
    private static long tStart;
    private static final int THREADS_COUNT = 1000;

    /**
     * @param args
     */
    public static void main(String[] args) {
        tStart = System.currentTimeMillis();
        doRun();
        System.out.println("Counter: " + resource.getCounter());
        System.out.println("Execution time: " + (System.currentTimeMillis() - tStart));
    }

    private static void doRun() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread accessing: " + Thread.currentThread().getName());
                for (int i = 0; i < 100; i++) {
                    updateCounter();
                }
                synchronized (threadsDone) {
                    threadsDone++;
                }
            }

            private void updateCounter() {
                synchronized (resource) {
                    resource.setCounter(resource.getCounter() + 1);
                    //                    System.out.println("Counter: " + resource.getCounter());
                }
            }
        };
        for (int i = 0; i < THREADS_COUNT; i++) {
            Thread t = new Thread(r);
            t.setName("Thread #" + (i + 1));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
