package com.smoke.communication;

import android.util.Log;

import com.smoke.communication.codecs.MessageRequestDecoder;
import com.smoke.communication.handlers.ChannelHandler;
import com.smoke.communication.protocol.MessageResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class CommunicationController extends ChannelInitializer<SocketChannel> {
    private ChannelHandler mChannelHandler;
    private ReadTimeoutHandler mReadTimeoutHandler;
    private int connectionPort;

    public static Bootstrap BOOTSTRAP;
    public static ChannelFuture SERVERCHANNEL;
    public static final int CHANNEL_MEMORY_BASE  = 0x400;
    public static final int CHANNEL_MEMORY_LIMIT = (0x1000 * 0x02);

    public void destruct()
    {
        this.mChannelHandler = null;
        this.mReadTimeoutHandler = null;
        CommunicationController.BOOTSTRAP.group().shutdownGracefully();
    }

    public boolean bootstrap()
    {
        this.mChannelHandler = new ChannelHandler();
        this.mReadTimeoutHandler = new ReadTimeoutHandler(60);
        return (this.mChannelHandler != null && this.mReadTimeoutHandler != null);
    }

    public boolean initializeNetworking() {
        return (CommunicationBootstrap.bootstrap(connectionPort));
    }


    public void flushResponse(MessageResponse response) {
        if (SERVERCHANNEL != null) {
            SERVERCHANNEL.channel().writeAndFlush(response.getPayload());
        }
    }

    public boolean isActive() {
        if (SERVERCHANNEL != null) {
            return(SERVERCHANNEL.channel().isActive());
        } else
            return false;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception
    {
        channel.pipeline().addLast("request_decoder", new MessageRequestDecoder());
        channel.pipeline().addLast("static_channel_handler", this.mChannelHandler);
        //channel.pipeline().addLast(new ReadTimeoutHandler(60));
    }
    // endregion

    // region #Constructors
    public CommunicationController(int connectionPort)
    {
        this.connectionPort = connectionPort;
    }
    // endregion
}
