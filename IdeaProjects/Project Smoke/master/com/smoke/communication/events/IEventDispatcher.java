package com.smoke.communication.events;

import com.smoke.communication.modules.Module;
import com.smoke.communication.protocol.MessageRequest;
import com.smoke.utilities.memory.IDisposable;

public interface IEventDispatcher extends IDisposable
{
    public int getLoadFactor();
    public int getEventQueueSize();
    public int getEventQueueLimit();
    public int getExecutorPoolSize();
    public void resizeEventQueueSize(int size);
    public void resizeExecutorPoolSize(int size);
    public void enqueue(Module session, MessageRequest request);
}