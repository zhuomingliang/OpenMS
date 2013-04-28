/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world.family;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import handling.world.World;
import java.util.logging.Logger;

public class FamilyLoad {

    public static final int NumSavingThreads = 8;
    private static final TimingThread[] Threads = new TimingThread[NumSavingThreads];

    static {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i] = new TimingThread(new FamilyLoadRunnable());
        }
    }
    private static final AtomicInteger Distribute = new AtomicInteger(0);

    public static void QueueFamilyForLoad(int hm) {
        int Current = Distribute.getAndIncrement() % NumSavingThreads;
        Threads[Current].getRunnable().Queue(Integer.valueOf(hm));
    }

    public static void Execute(Object ToNotify) {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].getRunnable().SetToNotify(ToNotify);
        }
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].start();
        }
    }

    private static class FamilyLoadRunnable implements Runnable {
        private Object ToNotify;
        private ArrayBlockingQueue<Integer> Queue = new ArrayBlockingQueue<Integer>(1000);

        public void run() {
            try {
                while (!Queue.isEmpty()) {
                    World.Family.addLoadedFamily(new MapleFamily(Queue.take()));
                }
                synchronized (ToNotify) {
                    ToNotify.notify();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(FamilyLoad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void Queue(Integer hm) {
            Queue.add(hm);
        }

        private void SetToNotify(Object o) {
            if (ToNotify == null) {
                ToNotify = o;
            }
        }
    }

    private static class TimingThread extends Thread {

        private final FamilyLoadRunnable ext;

        public TimingThread(FamilyLoadRunnable r) {
            super(r);
            ext = r;
        }

        public FamilyLoadRunnable getRunnable() {
            return ext;
        }
    }
}
