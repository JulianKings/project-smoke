package com.smoke.communication.client;

import com.smoke.communication.client.events.ClientEventDispatcher;
import com.smoke.communication.client.handlers.ChannelHandler;
import com.smoke.communication.codecs.MessageRequestDecoder;
import com.smoke.communication.events.IEventDispatcher;
import com.smoke.communication.protocol.MessageResponse;
import com.smoke.utilities.memory.IDisposable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientCommunicationController extends ChannelInitializer<SocketChannel> implements IDisposable {
    private ChannelHandler mChannelHandler;
    private IEventDispatcher mEventDispatcher;
    private int connectionPort;

    public static Bootstrap BOOTSTRAP;
    public static ChannelFuture SERVERCHANNEL;
    public static final int CHANNEL_MEMORY_BASE  = 0x400;
    public static final int CHANNEL_MEMORY_LIMIT = (0x1000 * 0x02);

    // region #Accesors
    public IEventDispatcher getEventDispatcher()
    {
        return this.mEventDispatcher;
    }
    // endregion

    // region #Methods
    @Override
    public void destruct()
    {
        this.mEventDispatcher.destruct();

        this.mChannelHandler = null;
        this.mEventDispatcher = null;

        ClientCommunicationController.BOOTSTRAP.group().shutdownGracefully();
    }

    public boolean bootstrap()
    {
        this.mChannelHandler = new ChannelHandler();

        return (this.mChannelHandler != null);
    }

    public boolean initializeNetworking()
    {
        return (ClientCommunicationBootstrap.bootstrap(connectionPort));
        /*
        {
            MessageResponse example = new MessageResponse((short)13);
            example.writeInteger(1);
            example.writeString("sup m8 im ur nigga proxy");
            SERVERCHANNEL.channel().writeAndFlush(example.getPayload());
            return true;
        }
        return false;*/
    }

    public void flushResponse(MessageResponse response) {
        if (SERVERCHANNEL != null) {
            SERVERCHANNEL.channel().writeAndFlush(response.getPayload());
        }
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception
    {
        channel.pipeline().addLast("request_decoder", new MessageRequestDecoder());
        channel.pipeline().addLast("static_channel_handler", this.mChannelHandler);
    }
    // endregion

    // region #Constructors
    public ClientCommunicationController(String listenerClass, int connectionPort)
    {
        this.connectionPort = connectionPort;
        this.mEventDispatcher = new ClientEventDispatcher(listenerClass);
    }
    // endregion
}
