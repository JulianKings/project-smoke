package com.smoke.communication;

// import io.netty.buffer.PooledByteBufAllocator;

import com.smoke.communication.modules.ModuleController;
import com.smoke.core.Environment;
import com.smoke.settings.Properties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;

public class CommunicationBootstrap {

    private static int CHANNEL_MEMORY_BASE;
    private static int CHANNEL_MEMORY_LIMIT;

    public static final byte BOSS_POOL_SIZE = 0x01;
    public static final byte WORKER_POOL_SIZE = 0x04;

    private static final String LOCALHOST = "0.0.0.0, 0:0:0:0:0:0:0:0";

    static {
        CHANNEL_MEMORY_BASE = CommunicationController.CHANNEL_MEMORY_BASE;
        CHANNEL_MEMORY_LIMIT = CommunicationController.CHANNEL_MEMORY_LIMIT;
    }

    public static boolean bootstrap(int connectionPort)
    {
        try
        {
            // TODO: Remove when no leaks are detected!
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);

            if (PlatformDependent.isWindows())
            {
                CommunicationController.BOOTSTRAP = new ServerBootstrap().group(
                    new NioEventLoopGroup(CommunicationBootstrap.BOSS_POOL_SIZE),
                    new NioEventLoopGroup(CommunicationBootstrap.WORKER_POOL_SIZE)
                );

                CommunicationController.BOOTSTRAP.channel(NioServerSocketChannel.class);
            }

            else
            {
                CommunicationController.BOOTSTRAP = new ServerBootstrap().group(
                    new EpollEventLoopGroup(CommunicationBootstrap.BOSS_POOL_SIZE),
                    new EpollEventLoopGroup(CommunicationBootstrap.WORKER_POOL_SIZE)
                );

                CommunicationController.BOOTSTRAP.channel(EpollServerSocketChannel.class);
            }

            CommunicationController.BOOTSTRAP.childHandler(Environment.getCommunication());

            CommunicationController.BOOTSTRAP.option(ChannelOption.SO_REUSEADDR, true);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.SO_LINGER, -1);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.SO_RCVBUF, 0x400);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.SO_SNDBUF, 0x1000);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.TCP_NODELAY, true);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.SO_KEEPALIVE, true);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK , CHANNEL_MEMORY_BASE);
            CommunicationController.BOOTSTRAP.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, CHANNEL_MEMORY_LIMIT);

            ModuleController.CHANNELS.bind(CommunicationController.BOOTSTRAP.bind(connectionPort).sync().channel());

            Environment.printOutBootInfo(String.format(
                "CommunicationController is accepting connections at hosts: {%s} and ports: {TCP#%d}.",
                CommunicationBootstrap.LOCALHOST, connectionPort
            ));
        }

        catch (Exception ex) { return false; }  finally { return true; }
    }
}