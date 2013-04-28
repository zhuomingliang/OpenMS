/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.shops;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emy
 */
public class HiredMerchantSave {

    public static final int NumSavingThreads = 5;
    private static final TimingThread[] Threads = new TimingThread[NumSavingThreads];

    static {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i] = new TimingThread(new HiredMerchantSaveRunnable());
        }
    }
    private static final AtomicInteger Distribute = new AtomicInteger(0);

    public static void QueueShopForSave(HiredMerchant hm) {
        int Current = Distribute.getAndIncrement() % NumSavingThreads;
        Threads[Current].getRunnable().Queue(hm);
    }

    public static void Execute(Object ToNotify) {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].getRunnable().SetToNotify(ToNotify);
        }
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].start();
        }
    }

    private static class HiredMerchantSaveRunnable implements Runnable {

        private static AtomicInteger RunningThreadID = new AtomicInteger(0);
        private int ThreadID = RunningThreadID.incrementAndGet();
        private long TimeTaken = 0;
        private int ShopsSaved = 0;
        private Object ToNotify;
        private ArrayBlockingQueue<HiredMerchant> Queue = new ArrayBlockingQueue<HiredMerchant>(500); //500 Start Capacity (Should be plenty)

        public void run() {
            try {
                while (!Queue.isEmpty()) {
                    HiredMerchant next = Queue.take();
                    long Start = System.currentTimeMillis();
                    next.closeShop(true, false);
                    TimeTaken += (System.currentTimeMillis() - Start);
                    ShopsSaved++;
                }
                System.out.println("[HiredMerchantSave Thread " + ThreadID + "] Shops Saved: " + ShopsSaved + " | Time Taken: " + TimeTaken + " Milliseconds");
                synchronized (ToNotify) {
                    ToNotify.notify();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(HiredMerchantSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void Queue(HiredMerchant hm) {
            Queue.add(hm);
        }

        private void SetToNotify(Object o) {
            if (ToNotify == null) {
                ToNotify = o;
            }
        }
    }

    private static class TimingThread extends Thread {

        private final HiredMerchantSaveRunnable ext;

        public TimingThread(HiredMerchantSaveRunnable r) {
            super(r);
            ext = r;
        }

        public HiredMerchantSaveRunnable getRunnable() {
            return ext;
        }
    }
}
