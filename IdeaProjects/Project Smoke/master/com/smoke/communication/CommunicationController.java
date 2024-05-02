package com.smoke.communication;

import com.smoke.communication.codecs.MessageRequestDecoder;
import com.smoke.communication.events.EventDispatcher;
import com.smoke.communication.events.IEventDispatcher;
import com.smoke.communication.handlers.ChannelHandler;
import com.smoke.communication.handlers.TrackingHandler;
import com.smoke.communication.modules.ModuleController;
import com.smoke.utilities.memory.IDisposable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class CommunicationController extends ChannelInitializer<SocketChannel> implements IDisposable {

    private ChannelHandler mChannelHandler;
    private TrackingHandler mTrackingHandler;
    private IEventDispatcher mEventDispatcher;
    private ModuleController mModuleController;
    private int connectionPort;

    public static ServerBootstrap BOOTSTRAP;
    public static final int CHANNEL_MEMORY_BASE  = 0x400;
    public static final int CHANNEL_MEMORY_LIMIT = (0x1000 * 0x02);

    // region #Accesors
    public ModuleController getModules()
    {
        return this.mModuleController;
    }

    public TrackingHandler getNetworkProfiler()
    {
        return this.mTrackingHandler;
    }

    public IEventDispatcher getEventDispatcher()
    {
        return this.mEventDispatcher;
    }
    // endregion

    // region #Methods
    @Override
    public void destruct()
    {
        this.mTrackingHandler.release();
        this.mEventDispatcher.destruct();
        this.mModuleController.destruct();

        this.mChannelHandler = null;
        this.mEventDispatcher = null;
        this.mTrackingHandler = null;
        this.mModuleController = null;

        CommunicationController.BOOTSTRAP.group().shutdownGracefully();
        CommunicationController.BOOTSTRAP.childGroup().shutdownGracefully();
    }

    public boolean bootstrap()
    {
        this.mChannelHandler = new ChannelHandler();
        this.mTrackingHandler = new TrackingHandler(GlobalEventExecutor.INSTANCE);

        return (this.mChannelHandler != null && this.mTrackingHandler != null);
    }

    public boolean initializeNetworking()
    {
        return CommunicationBootstrap.bootstrap(connectionPort);
    }

    public boolean initializeNetworkProfiler()
    {
        this.mTrackingHandler.trafficCounter().start(); return true;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception
    {
        channel.pipeline().addLast("net_profiler", this.mTrackingHandler);
        channel.pipeline().addLast("request_decoder", new MessageRequestDecoder());
        channel.pipeline().addLast("static_channel_handler", this.mChannelHandler);
    }
    // endregion

    // region #Constructors
    public CommunicationController(String listenerClass, int connectionPort)
    {
        this.connectionPort = connectionPort;
        this.mEventDispatcher = new EventDispatcher(listenerClass);
        this.mModuleController = new ModuleController();
    }
    // endregion
}
