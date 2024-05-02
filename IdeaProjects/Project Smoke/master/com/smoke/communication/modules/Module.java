package com.smoke.communication.modules;

import com.smoke.communication.codecs.BinaryDecoder;
import com.smoke.communication.protocol.MessageRequest;
import com.smoke.communication.protocol.MessageResponse;
import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

public class Module {

    private long iID;
    private Channel mChannel;
    private String sIPAddress;
    private long iLastReadTime;
    private static AtomicLong MODULE_ID;

    static {
        Module.MODULE_ID = new AtomicLong();
    }

    public long getID()
    {
        return this.iID;
    }

    public Channel getChannel()
    {
        return this.mChannel;
    }

    public String getIPAddress()
    {
        return this.sIPAddress;
    }

    public long getLastReadTime()
    {
        return this.iLastReadTime;
    }

    public void fireOnlineTriggers()
    {

    }

    public void fireOfflineTriggers()
    {

    }

    public Module construct(Channel channel)
    {
        this.mChannel = channel;
        this.iID = Module.MODULE_ID.incrementAndGet();
        this.sIPAddress = ((InetSocketAddress) channel.
        remoteAddress()).getAddress().getHostAddress();
        this.iLastReadTime = Environment.traceMilliTime();
        return this;
    }

    public void read(MessageRequest request)
    {
        this.iLastReadTime = Environment.traceMilliTime();
        Environment.getLogger().printOut(LogLevel.DEBUG, "[" + this.iID + "|RCV]" +
        "[#" + request.getOPCode() + "]: " + BinaryDecoder.parse(request.toString()));
    }

    public void write(MessageResponse response)
    {
        Environment.getLogger().printOut(LogLevel.DEBUG, "[" + this.iID + "|SND]" +
        "[#" + response.getOPCode() + "]: " + BinaryDecoder.parse(response.toString()));
        this.mChannel.writeAndFlush(response.getPayload().duplicate().retain(), this.mChannel.voidPromise());
    }
}
