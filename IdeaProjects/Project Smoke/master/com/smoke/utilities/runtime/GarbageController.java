package com.smoke.utilities.runtime;

import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;

public class GarbageController implements Runnable {

    private boolean IS_ALIVE;
    private Thread tCollector;
    private final int SLEEP_TIME = 300000;

    public void start()
    {
        this.IS_ALIVE = true;
        this.tCollector.start();
    }

    public void stop()
    {
        this.IS_ALIVE = false;
        this.tCollector.interrupt();
    }

    public GarbageController()
    {
        this.tCollector = new Thread(this);
        this.tCollector.setPriority(Thread.MIN_PRIORITY);
        this.tCollector.setName("GARBAGE-MONITOR-THREAD-0");

        this.start();
    }

    public void forceCollection()
    {
        System.gc();
        System.runFinalization();
    }

    @Override
    public void run()
    {
        while (this.IS_ALIVE)
        {
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            Environment.getLogger().printOut(LogLevel.DEBUG, "JVM memory usage: " +
            ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 20) + " MB");

            try
            {
                Thread.sleep(this.SLEEP_TIME);
            }

            catch (InterruptedException ex)
            {
                Environment.getLogger().printOut(LogLevel.CRITICAL, "GarbageController.run() has thrown an exception.", ex);
            }
        }
    }
}
