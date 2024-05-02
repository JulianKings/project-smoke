package com.smoke.communication.client.events;

import com.smoke.communication.events.IEventDispatcher;
import com.smoke.communication.events.IEventListener;
import com.smoke.communication.modules.Module;
import com.smoke.communication.protocol.MessageRequest;
import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;
import com.smoke.settings.Properties;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientEventDispatcher implements IEventDispatcher, Runnable {
    private int iQueueLimit;
    private boolean bIsActive;
    private String sIdentifier;
    private Executor mExecutor;
    private BlockingQueue mQueue;
    private String sListenerClass;
    private static AtomicInteger THREAD_ID;
    private static AtomicInteger DISPATCHER_ID;


    static
    {
        ClientEventDispatcher.THREAD_ID = new AtomicInteger();
        ClientEventDispatcher.DISPATCHER_ID = new AtomicInteger();
    }

    @Override
    public void run()
    {
        IEventListener listener = null;
        Thread.currentThread().setName(sIdentifier + "-THREAD-" + ClientEventDispatcher.THREAD_ID.getAndIncrement());

        try
        {
            listener = (IEventListener) Class.forName(sListenerClass).newInstance();
        }

        catch (Exception ex)
        {
            Environment.printOutBootError("IEventDispatcher has thrown an exception.", ex);
        }

        while (this.bIsActive)
        {
            try
            {
                long start, pause;
                MessageRequest request = (MessageRequest) this.mQueue.take();
                start = Environment.traceNanoTime(); listener.invoke(request);
                pause = Environment.traceNanoTime(); request.destruct(); // kill

                if (Properties.MESSAGE_REQUEST_PROFILING)
                {
                    Environment.getLogger().printOut(LogLevel.DEBUG, "Took " + ((pause - start) / 1000000.0D) + "ms to process the request.");
                }
            }

            catch ( Exception ex)
            {
                Environment.getLogger().printOut(LogLevel.CRITICAL, sIdentifier + " has thrown an exception.", ex);
            }
        }

    }

    @Override
    public void destruct()
    {
        this.mQueue.clear();
        this.bIsActive = false;
        Environment.getThreadController().purgeNetwork();

        this.mQueue = null;
        this.mExecutor = null;
        this.sIdentifier = null;
        this.sListenerClass = null;
    }

    public ClientEventDispatcher(String eventListenerClass)
    {
        this.sListenerClass = eventListenerClass;
        this.bIsActive = true;
        this.iQueueLimit = Properties.DISPATCHER_QUEUE_LIMIT;
        this.sIdentifier = "DISPATCHER-" + DISPATCHER_ID.getAndIncrement();
        this.mQueue = new ArrayBlockingQueue(Properties.DISPATCHER_QUEUE_LIMIT, false);

        for (int i = 0; i < Properties.DISPATCHER_EXECUTOR_SIZE; i++) { Environment.getThreadController().executePacket(this); }
    }

    @Override
    public int getLoadFactor()
    {
        return (this.mQueue.size()
                / this.iQueueLimit) * 100;
    }

    @Override
    public int getEventQueueSize()
    {
        return this.mQueue.size();
    }

    @Override
    public int getEventQueueLimit()
    {
        return this.iQueueLimit;
    }

    @Override
    public int getExecutorPoolSize()
    {
        return Properties.DISPATCHER_EXECUTOR_SIZE;
    }

    @Override
    public void resizeEventQueueSize(int size)
    {
        if (size <= this.iQueueLimit) { return; }
        BlockingQueue queue = new ArrayBlockingQueue(size, false);
        this.iQueueLimit = size; this.mQueue.drainTo(queue); this.mQueue = queue;
    }

    @Override
    public void resizeExecutorPoolSize(int size)
    {
        // TODO resize pool
    }

    @Override
    public void enqueue(Module module, MessageRequest request)
    {
        request.setModule(module);
        if (this.iQueueLimit > this.mQueue.size()) { this.mQueue.add(request); return; } request.destruct();
        Environment.getLogger().printOut(LogLevel.WARNING, "The request queue is full, dropping incoming request...");
    }
}
