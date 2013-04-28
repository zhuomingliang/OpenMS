/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world.guild;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import handling.world.World;
import handling.world.guild.MapleBBSThread.MapleBBSReply;
import java.util.Map;
import java.util.logging.Logger;

public class GuildLoad {

    public static final int NumSavingThreads = 6;
    private static Map<Integer, Map<Integer, MapleBBSReply>> replies = null;
    private static final TimingThread[] Threads = new TimingThread[NumSavingThreads];

    static {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i] = new TimingThread(new GuildLoadRunnable());
        }
    }
    private static final AtomicInteger Distribute = new AtomicInteger(0);

    public static void QueueGuildForLoad(int hm, Map<Integer, Map<Integer, MapleBBSReply>> replie) {
        int Current = Distribute.getAndIncrement() % NumSavingThreads;
        Threads[Current].getRunnable().Queue(Integer.valueOf(hm));
	if (replies == null) {
	    replies = replie;
	}
    }

    public static void Execute(Object ToNotify) {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].getRunnable().SetToNotify(ToNotify);
        }
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].start();
        }
    }

    private static class GuildLoadRunnable implements Runnable {
        private Object ToNotify;
        private ArrayBlockingQueue<Integer> Queue = new ArrayBlockingQueue<Integer>(1000); //1000 Start Capacity (Should be plenty)

        public void run() {
            try {
                while (!Queue.isEmpty()) {
                    World.Guild.addLoadedGuild(new MapleGuild(Queue.take(), replies));
                }
                synchronized (ToNotify) {
                    ToNotify.notify();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GuildLoad.class.getName()).log(Level.SEVERE, null, ex);
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

        private final GuildLoadRunnable ext;

        public TimingThread(GuildLoadRunnable r) {
            super(r);
            ext = r;
        }

        public GuildLoadRunnable getRunnable() {
            return ext;
        }
    }
}
