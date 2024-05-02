package com.smoke.communication.client;

import com.smoke.communication.CommunicationController;
import com.smoke.core.Environment;
import com.smoke.settings.Properties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;

public class ClientCommunicationBootstrap {
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
                ClientCommunicationController.BOOTSTRAP = new Bootstrap().group(
                        new NioEventLoopGroup(ClientCommunicationBootstrap.BOSS_POOL_SIZE)
                );

                ClientCommunicationController.BOOTSTRAP.channel(NioSocketChannel.class);
            }

            else
            {
                ClientCommunicationController.BOOTSTRAP = new Bootstrap().group(
                        new EpollEventLoopGroup(ClientCommunicationBootstrap.BOSS_POOL_SIZE)
                );

                ClientCommunicationController.BOOTSTRAP.channel(EpollSocketChannel.class);
            }

            ClientCommunicationController.BOOTSTRAP.handler(Environment.getClientCommunication());

            ClientCommunicationController.BOOTSTRAP.option(ChannelOption.SO_REUSEADDR, true);
            ClientCommunicationController.BOOTSTRAP.option(ChannelOption.SO_RCVBUF, 0x400);
            ClientCommunicationController.BOOTSTRAP.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            ClientCommunicationController.BOOTSTRAP.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, CHANNEL_MEMORY_BASE);
            ClientCommunicationController.BOOTSTRAP.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, CHANNEL_MEMORY_LIMIT);

            ClientCommunicationController.SERVERCHANNEL = ClientCommunicationController.BOOTSTRAP.connect("127.0.0.1", connectionPort).sync();

            Environment.printOutBootInfo(String.format(
                    "ClientCommunicationController is connected to host: {%s} and port: {TCP#%d}.",
                    ClientCommunicationBootstrap.LOCALHOST, connectionPort
            ));
        }

        catch (Exception ex) { Environment.printOutBootError("Error initializing ClientCommunicationController", ex); return false; } finally { return true; }
    }
}