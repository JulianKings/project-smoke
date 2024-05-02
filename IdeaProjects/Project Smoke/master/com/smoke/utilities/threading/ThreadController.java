package com.smoke.utilities.threading;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.smoke.settings.Properties;
import com.smoke.utilities.classes.StringUtil;


public class ThreadController
{
    protected static final Logger _log = Logger.getLogger(ThreadController.class.getName());

    private static final class RunnableWrapper implements Runnable
    {
        private final Runnable _r;

        public RunnableWrapper(final Runnable r)
        {
            _r = r;
        }

        @Override
        public final void run()
        {
            try
            {
                _r.run();
            }
            catch (final Throwable e)
            {
                final Thread t = Thread.currentThread();
                final UncaughtExceptionHandler h = t.getUncaughtExceptionHandler();
                if (h != null)
                {
                    h.uncaughtException(t, e);
                }
            }
        }
    }

    protected ScheduledThreadPoolExecutor mGeneralScheduledThreadPool;
    protected ScheduledThreadPoolExecutor mAIScheduledThreadPool;
    private final ThreadPoolExecutor mGeneralPacketsThreadPool;
    private final ThreadPoolExecutor mIOPacketsThreadPool;
    private final ThreadPoolExecutor mGeneralThreadPool;

    private boolean _shutdown;

    public ThreadController()
    {
        mGeneralScheduledThreadPool = new ScheduledThreadPoolExecutor(Properties.THREADING_GENERAL_SCHEDULER_SIZE, new PriorityThreadFactory("GeneralSTPool", Thread.NORM_PRIORITY));
        mIOPacketsThreadPool = new ThreadPoolExecutor(Properties.THREADING_IO_POOL_SIZE, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("I/O Packet Pool", Thread.NORM_PRIORITY + 1));
        mGeneralPacketsThreadPool = new ThreadPoolExecutor(Properties.THREADING_PACKET_POOL_SIZE, Properties.THREADING_PACKET_POOL_SIZE + 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Normal Packet Pool", Thread.NORM_PRIORITY + 1));
        mGeneralThreadPool = new ThreadPoolExecutor(Properties.THREADING_GENERAL_POOL_SIZE, Properties.THREADING_PACKET_POOL_SIZE + 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
        mAIScheduledThreadPool = new ScheduledThreadPoolExecutor(Properties.THREADING_AI_SCHEDULER_SIZE, new PriorityThreadFactory("AISTPool", Thread.NORM_PRIORITY));

        scheduleGeneralAtFixedRate(new PurgeTask(), 10, 5, TimeUnit.MINUTES);
    }

    /**
     * Schedules a general task to be executed after the given delay.
     * @param task the task to execute
     * @param delay the delay in the given time unit
     * @param unit the time unit of the delay parameter
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay, TimeUnit unit)
    {
        try
        {
            return mGeneralScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
        }
        catch (RejectedExecutionException e)
        {
            return null; /* shutdown, ignore */
        }
    }

    /**
     * Schedules a general task to be executed after the given delay.
     * @param task the task to execute
     * @param delay the delay in milliseconds
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay)
    {
        return scheduleGeneral(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a general task to be executed at fixed rate.
     * @param task the task to execute
     * @param initialDelay the initial delay in the given time unit
     * @param period the period between executions in the given time unit
     * @param unit the time unit of the initialDelay and period parameters
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)
    {
        try
        {
            return mGeneralScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
        }
        catch (RejectedExecutionException e)
        {
            return null; /* shutdown, ignore */
        }
    }
    /**
     * Schedules a general task to be executed at fixed rate.
     * @param task the task to execute
     * @param initialDelay the initial delay in milliseconds
     * @param period the period between executions in milliseconds
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable task, long initialDelay, long period)
    {
        return scheduleGeneralAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules an AI task to be executed after the given delay.
     * @param task the task to execute
     * @param delay the delay in the given time unit
     * @param unit the time unit of the delay parameter
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleAi(Runnable task, long delay, TimeUnit unit)
    {
        try
        {
            return mAIScheduledThreadPool.schedule(new RunnableWrapper(task), delay, unit);
        }
        catch (RejectedExecutionException e)
        {
            return null; /* shutdown, ignore */
        }
    }

    /**
     * Schedules an AI task to be executed after the given delay.
     * @param task the task to execute
     * @param delay the delay in milliseconds
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleAi(Runnable task, long delay)
    {
        return scheduleAi(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a general task to be executed at fixed rate.
     * @param task the task to execute
     * @param initialDelay the initial delay in the given time unit
     * @param period the period between executions in the given time unit
     * @param unit the time unit of the initialDelay and period parameters
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)
    {
        try
        {
            return mAIScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(task), initialDelay, period, unit);
        }
        catch (RejectedExecutionException e)
        {
            return null; /* shutdown, ignore */
        }
    }

    /**
     * Schedules a general task to be executed at fixed rate.
     * @param task the task to execute
     * @param initialDelay the initial delay in milliseconds
     * @param period the period between executions in milliseconds
     * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation
     */
    public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable task, long initialDelay, long period)
    {
        return scheduleAiAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a packet task sometime in future in another thread.
     * @param task the task to execute
     */
    public void executePacket(Runnable task)
    {
        try
        {
            mGeneralPacketsThreadPool.execute(task);
        }
        catch (RejectedExecutionException e)
        {
			/* shutdown, ignore */
        }
    }

    /**
     * Executes an IO packet task sometime in future in another thread.
     * @param task the task to execute
     */
    public void executeIOPacket(Runnable task)
    {
        try
        {
            mIOPacketsThreadPool.execute(task);
        }
        catch (RejectedExecutionException e)
        {
			/* shutdown, ignore */
        }
    }

    /**
     * Executes a general task sometime in future in another thread.
     * @param task the task to execute
     */
    public void executeGeneral(Runnable task)
    {
        try
        {
            mGeneralThreadPool.execute(new RunnableWrapper(task));
        }
        catch (RejectedExecutionException e)
        {
			/* shutdown, ignore */
        }
    }

    /**
     * Executes an AI task sometime in future in another thread.
     * @param task the task to execute
     */
    public void executeAi(Runnable task)
    {
        try
        {
            mAIScheduledThreadPool.execute(new RunnableWrapper(task));
        }
        catch (RejectedExecutionException e)
        {
			/* shutdown, ignore */
        }
    }

    public String[] getStats()
    {
        return new String[]
                {
                        "STP:",
                        " + General:",
                        " |- ActiveThreads:   " + mGeneralScheduledThreadPool.getActiveCount(),
                        " |- getCorePoolSize: " + mGeneralScheduledThreadPool.getCorePoolSize(),
                        " |- PoolSize:        " + mGeneralScheduledThreadPool.getPoolSize(),
                        " |- MaximumPoolSize: " + mGeneralScheduledThreadPool.getMaximumPoolSize(),
                        " |- CompletedTasks:  " + mGeneralScheduledThreadPool.getCompletedTaskCount(),
                        " |- ScheduledTasks:  " + mGeneralScheduledThreadPool.getQueue().size(),
                        " | -------",
                        " + AI:",
                        " |- ActiveThreads:   " + mAIScheduledThreadPool.getActiveCount(),
                        " |- getCorePoolSize: " + mAIScheduledThreadPool.getCorePoolSize(),
                        " |- PoolSize:        " + mAIScheduledThreadPool.getPoolSize(),
                        " |- MaximumPoolSize: " + mAIScheduledThreadPool.getMaximumPoolSize(),
                        " |- CompletedTasks:  " + mAIScheduledThreadPool.getCompletedTaskCount(),
                        " |- ScheduledTasks:  " + mAIScheduledThreadPool.getQueue().size(),
                        "TP:",
                        " + Packets:",
                        " |- ActiveThreads:   " + mGeneralPacketsThreadPool.getActiveCount(),
                        " |- getCorePoolSize: " + mGeneralPacketsThreadPool.getCorePoolSize(),
                        " |- MaximumPoolSize: " + mGeneralPacketsThreadPool.getMaximumPoolSize(),
                        " |- LargestPoolSize: " + mGeneralPacketsThreadPool.getLargestPoolSize(),
                        " |- PoolSize:        " + mGeneralPacketsThreadPool.getPoolSize(),
                        " |- CompletedTasks:  " + mGeneralPacketsThreadPool.getCompletedTaskCount(),
                        " |- QueuedTasks:     " + mGeneralPacketsThreadPool.getQueue().size(),
                        " | -------",
                        " + I/O Packets:",
                        " |- ActiveThreads:   " + mIOPacketsThreadPool.getActiveCount(),
                        " |- getCorePoolSize: " + mIOPacketsThreadPool.getCorePoolSize(),
                        " |- MaximumPoolSize: " + mIOPacketsThreadPool.getMaximumPoolSize(),
                        " |- LargestPoolSize: " + mIOPacketsThreadPool.getLargestPoolSize(),
                        " |- PoolSize:        " + mIOPacketsThreadPool.getPoolSize(),
                        " |- CompletedTasks:  " + mIOPacketsThreadPool.getCompletedTaskCount(),
                        " |- QueuedTasks:     " + mIOPacketsThreadPool.getQueue().size(),
                        " | -------",
                        " + General Tasks:",
                        " |- ActiveThreads:   " + mGeneralThreadPool.getActiveCount(),
                        " |- getCorePoolSize: " + mGeneralThreadPool.getCorePoolSize(),
                        " |- MaximumPoolSize: " + mGeneralThreadPool.getMaximumPoolSize(),
                        " |- LargestPoolSize: " + mGeneralThreadPool.getLargestPoolSize(),
                        " |- PoolSize:        " + mGeneralThreadPool.getPoolSize(),
                        " |- CompletedTasks:  " + mGeneralThreadPool.getCompletedTaskCount(),
                        " |- QueuedTasks:     " + mGeneralThreadPool.getQueue().size(),
                        " | -------",
                };
    }

    private static class PriorityThreadFactory implements ThreadFactory
    {
        private final int _prio;
        private final String _name;
        private final AtomicInteger _threadNumber = new AtomicInteger(1);
        private final ThreadGroup _group;

        public PriorityThreadFactory(String name, int prio)
        {
            _prio = prio;
            _name = name;
            _group = new ThreadGroup(_name);
        }

        @Override
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(_group, r, _name + "-" + _threadNumber.getAndIncrement());
            t.setPriority(_prio);
            return t;
        }

        public ThreadGroup getGroup()
        {
            return _group;
        }
    }

    public void shutdown()
    {
        _shutdown = true;
        try
        {
            mGeneralScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            mGeneralPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            mIOPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            mGeneralThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            mGeneralScheduledThreadPool.shutdown();
            mGeneralPacketsThreadPool.shutdown();
            mIOPacketsThreadPool.shutdown();
            mGeneralThreadPool.shutdown();
            _log.info("All ThreadPools are now stopped");

        }
        catch (InterruptedException e)
        {
            _log.log(Level.WARNING, "", e);
        }
    }

    public boolean isShutdown()
    {
        return _shutdown;
    }

    public void purge()
    {
        mGeneralScheduledThreadPool.purge();
        mAIScheduledThreadPool.purge();
        mIOPacketsThreadPool.purge();
        mGeneralPacketsThreadPool.purge();
        mGeneralThreadPool.purge();
    }

    public void purgeNetwork()
    {
        mIOPacketsThreadPool.purge();
        mGeneralPacketsThreadPool.purge();
    }

    public String getPacketStats()
    {
        final StringBuilder sb = new StringBuilder(1000);
        ThreadFactory tf = mGeneralPacketsThreadPool.getThreadFactory();
        if (tf instanceof PriorityThreadFactory)
        {
            PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
            int count = ptf.getGroup().activeCount();
            Thread[] threads = new Thread[count + 2];
            ptf.getGroup().enumerate(threads);
            StringUtil.append(sb, "General Packet Thread Pool:" + "\n" + "Tasks in the queue: ", String.valueOf(mGeneralPacketsThreadPool.getQueue().size()), "\n" + "Showing threads stack trace:" + "\n" + "There should be ", String.valueOf(count), " Threads" + "\n");
            for (Thread t : threads)
            {
                if (t == null)
                {
                    continue;
                }

                StringUtil.append(sb, t.getName(), "\n");
                for (StackTraceElement ste : t.getStackTrace())
                {
                    StringUtil.append(sb, ste.toString(), "\n");
                }
            }
        }

        sb.append("Packet Tp stack traces printed.");
        sb.append("\n");
        return sb.toString();
    }

    public String getIOPacketStats()
    {
        final StringBuilder sb = new StringBuilder(1000);
        ThreadFactory tf = mIOPacketsThreadPool.getThreadFactory();

        if (tf instanceof PriorityThreadFactory)
        {
            PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
            int count = ptf.getGroup().activeCount();
            Thread[] threads = new Thread[count + 2];
            ptf.getGroup().enumerate(threads);
            StringUtil.append(sb, "I/O Packet Thread Pool:" + "\n" + "Tasks in the queue: ", String.valueOf(mIOPacketsThreadPool.getQueue().size()), "\n" + "Showing threads stack trace:" + "\n" + "There should be ", String.valueOf(count), " Threads" + "\n");

            for (Thread t : threads)
            {
                if (t == null)
                {
                    continue;
                }

                StringUtil.append(sb, t.getName(), "\n");

                for (StackTraceElement ste : t.getStackTrace())
                {
                    StringUtil.append(sb, ste.toString(), "\n");
                }
            }
        }

        sb.append("Packet Tp stack traces printed." + "\n");

        return sb.toString();
    }

    public String getGeneralStats()
    {
        final StringBuilder sb = new StringBuilder(1000);
        ThreadFactory tf = mGeneralThreadPool.getThreadFactory();

        if (tf instanceof PriorityThreadFactory)
        {
            PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
            int count = ptf.getGroup().activeCount();
            Thread[] threads = new Thread[count + 2];
            ptf.getGroup().enumerate(threads);
            StringUtil.append(sb, "General Thread Pool:" + "\n" + "Tasks in the queue: ", String.valueOf(mGeneralThreadPool.getQueue().size()), "\n" + "Showing threads stack trace:" + "\n" + "There should be ", String.valueOf(count), " Threads" + "\n");

            for (Thread t : threads)
            {
                if (t == null)
                {
                    continue;
                }

                StringUtil.append(sb, t.getName(), "\n");

                for (StackTraceElement ste : t.getStackTrace())
                {
                    StringUtil.append(sb, ste.toString(), "\n");
                }
            }
        }

        sb.append("Packet Tp stack traces printed." + "\n");

        return sb.toString();
    }

    protected class PurgeTask implements Runnable
    {
        @Override
        public void run()
        {
            mGeneralScheduledThreadPool.purge();
            mAIScheduledThreadPool.purge();
        }
    }

    private static class SingletonHolder
    {
        protected static final ThreadController _instance = new ThreadController();
    }
}
