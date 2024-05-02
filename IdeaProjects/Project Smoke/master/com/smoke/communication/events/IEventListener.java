package com.smoke.communication.events;

import com.smoke.communication.protocol.MessageRequest;

public interface IEventListener
{
    public String getIdentifier();
    public void invoke(MessageRequest request);
}