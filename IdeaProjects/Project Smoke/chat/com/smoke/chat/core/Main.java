package com.smoke.chat.core;

import com.smoke.communication.protocol.MessageResponse;
import com.smoke.core.Environment;
import com.smoke.core.ServerType;

public class Main {
    public static void main(String[] args)
    {
        // sup
        if(Environment.bootstrap(args[0] + ";" + args[1], ServerType.CHAT,
                "com.smoke.chat.communication.client.events.ClientEventResolver;" +
                "com.smoke.chat.communication.server.events.EventResolver"))
        {
            // Server initialized properly
            MessageResponse example = new MessageResponse((short)13);
            example.writeInteger(1);
            example.writeString("sup m8 im ur nigga chat");
            Environment.getClientCommunication().flushResponse(example);
        }
    }
}
