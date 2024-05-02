package com.smoke.communication.handlers;

import com.smoke.communication.modules.Module;
import com.smoke.communication.modules.ModuleController;
import com.smoke.communication.protocol.MessageRequest;
import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;
import com.smoke.settings.Properties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends ChannelInboundHandlerAdapter {

    public static AttributeKey<String> KEY_MACHINE;
    public static AttributeKey<Module> KEY_MODULE;

    static {
        ChannelHandler.KEY_MODULE = AttributeKey.valueOf("channel.module");
        ChannelHandler.KEY_MACHINE = AttributeKey.valueOf("channel.machine");
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception
    {
        super.channelActive(context);

        ModuleController.CHANNELS.bind(context.channel());

        context.attr(ChannelHandler.KEY_MODULE).set(
            Environment.getCommunication().getModules().addConnection(context.channel())
        );
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception
    {
        super.channelInactive(context);

        ModuleController.CHANNELS.unbind(context.channel());
        Environment.getCommunication().getModules().removeConnection(context.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception
    {
        super.channelRead(context, message);

        long start = Environment.traceNanoTime();

        if (message instanceof MessageRequest)
        {
            MessageRequest request = (MessageRequest) message;
            Module module = context.attr(KEY_MODULE).get(); module.read(request);
            Environment.getCommunication().getEventDispatcher().enqueue(module, request);
        }

        long pause = Environment.traceNanoTime();

        if (Properties.MESSAGE_REQUEST_PROFILING)
        {
            Environment.getLogger().printOut(LogLevel.TRACE, "Took " + (pause - start) /  1000000.0d + "ms to enqueue the request.");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception
    {
        final Throwable inner = cause.getCause();

        if (inner instanceof ClosedChannelException || !(inner instanceof IOException))
        {
            Environment.getLogger().printOut(LogLevel.CRITICAL, "ChannelHandler has intercepted an Exception.", cause);
        }
    }
}