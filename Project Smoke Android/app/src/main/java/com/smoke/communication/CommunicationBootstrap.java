package com.smoke.communication;

import com.smoke.core.Environment;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;

import java.net.InetAddress;

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
            CommunicationController.BOOTSTRAP = new Bootstrap().group(
                    new NioEventLoopGroup(CommunicationBootstrap.BOSS_POOL_SIZE)
            );

            CommunicationController.BOOTSTRAP.channel(NioSocketChannel.class);

            CommunicationController.BOOTSTRAP.handler(Environment.getCommunication());

            CommunicationController.BOOTSTRAP.option(ChannelOption.SO_REUSEADDR, true);
            CommunicationController.BOOTSTRAP.option(ChannelOption.SO_RCVBUF, 0x400);
            CommunicationController.BOOTSTRAP.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            CommunicationController.BOOTSTRAP.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, CHANNEL_MEMORY_BASE);
            CommunicationController.BOOTSTRAP.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, CHANNEL_MEMORY_LIMIT);

            CommunicationController.SERVERCHANNEL = CommunicationController.BOOTSTRAP.connect((InetAddress
                    .getByName("192.168.0.26")), connectionPort).sync();

            //CommunicationController.SERVERCHANNEL = CommunicationController.BOOTSTRAP.connect((InetAddress
            //        .getByName("10.0.2.2")), connectionPort).sync();

            /*Environment.printOutBootInfo(String.format(
                    "CommunicationController is connected to host: {%s} and port: {TCP#%d}.",
                    CommunicationBootstrap.LOCALHOST, connectionPort
            ));*/
            return true;
        }
        catch (Exception ex) { System.out.println(ex.getMessage()); ex.printStackTrace();  /*Environment.printOutBootError("Error initializing CommunicationController", ex);*/ return false; }
    }
}