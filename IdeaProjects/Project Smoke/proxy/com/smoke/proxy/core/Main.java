package com.smoke.proxy.core;

import com.smoke.communication.protocol.MessageResponse;
import com.smoke.core.Environment;
import com.smoke.core.ServerType;

public class Main {
    public static void main(String[] args)
    {
        // sup
        if(Environment.bootstrap(args[0] + ";" + args[1], ServerType.PROXY,
                "com.smoke.proxy.communication.client.events.ClientEventResolver;" +
                "com.smoke.proxy.communication.server.events.EventResolver"))
        {
            // Server initialized properly
            MessageResponse example = new MessageResponse((short)13);
            example.writeInteger(1);
            example.writeString("sup m8 im ur nigga proxy");
            Environment.getClientCommunication().flushResponse(example);
        }
    }
}
