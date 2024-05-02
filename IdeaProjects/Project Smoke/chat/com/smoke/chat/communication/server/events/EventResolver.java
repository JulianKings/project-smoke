package com.smoke.chat.communication.server.events;

import com.smoke.communication.events.IEventListener;
import com.smoke.communication.events.IMessageEvent;
import com.smoke.communication.opcodes.MasterEOPCodes;
import com.smoke.communication.protocol.MessageRequest;
import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;

import java.util.concurrent.atomic.AtomicInteger;

public class EventResolver implements IEventListener {
    private String sIdentifier;
    private static IMessageEvent[] INCOMING;
    private static IMessageEvent[] RESOLVER;
    private static AtomicInteger ID_LISTENER;

    private static final short REQUEST_BOUNDARY = 0xfa1;

    static
    {
        MasterEOPCodes.defineIOP();
        MasterEOPCodes.defineOOP();
    }

    static
    {
        EventResolver.ID_LISTENER = new AtomicInteger();
    }

    static
    {
        EventResolver.INCOMING = new IMessageEvent[REQUEST_BOUNDARY];
        EventResolver.RESOLVER = new IMessageEvent[REQUEST_BOUNDARY];
    }

    // region #Constructors
    public EventResolver()
    {
        EventResolver.construct();
        this.sIdentifier = "EVENT-LISTENER-" + EventResolver.ID_LISTENER.getAndIncrement();
    }

    private static void construct()
    {

        // com.habbo.communication.incoming.messenger
        // EventResolver.RESOLVER[NewMMSMessageEvent.OPCODE] = new NewMMSMessageEvent();
        // EventResolver.RESOLVER[AcceptBuddyMessageEvent.OPCODE] = new AcceptBuddyMessageEvent();
        // EventResolver.RESOLVER[RemoveBuddyMessageEvent.OPCODE] = new RemoveBuddyMessageEvent();
        // EventResolver.RESOLVER[SearchBuddyMessageEvent.OPCODE] = new SearchBuddyMessageEvent();
        // EventResolver.RESOLVER[DeclineBuddyMessageEvent.OPCODE] = new DeclineBuddyMessageEvent();
        // EventResolver.RESOLVER[NewBuddyLinkMessageEvent.OPCODE] = new NewBuddyLinkMessageEvent();
        // EventResolver.RESOLVER[NewRoomInviteMessageEvent.OPCODE] = new NewRoomInviteMessageEvent();
        // EventListener.RESOLVER[BuddyRequestsMessageEvent.OPCODE] = new BuddyRequestsMessageEvent();
        // EventListener.RESOLVER[MessengerInitMessageEvent.OPCODE] = new MessengerInitMessageEvent();
    }
    // endregion

    // region #Accessors
    @Override
    public String getIdentifier()
    {
        return this.sIdentifier;
    }
    // endregion

    // region #Methods
    @Override
    public void invoke(MessageRequest request)
    {
        short OPCode = request.getOPCode();

        if (EventResolver.INCOMING[OPCode] != null)
        {
            // Then parse incoming request without checks
            EventResolver.INCOMING[OPCode].parse(request);
        }
        else if (EventResolver.RESOLVER[OPCode] != null) {
            // Parse incoming MessageRequest
            EventResolver.RESOLVER[OPCode].parse(request);
        }
        else // if (EventResolver.RESOLVER[OPCode] == null)
        {
            Environment.getLogger().printOut(LogLevel.DEBUG, "EventListener can't recognize the following event OPCode: " + OPCode);
        }
    }
}
