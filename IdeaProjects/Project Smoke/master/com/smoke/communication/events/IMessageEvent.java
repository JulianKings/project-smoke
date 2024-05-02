package com.smoke.communication.events;

import com.smoke.communication.protocol.MessageRequest;

public interface IMessageEvent
{
    public void parse(MessageRequest request);
}