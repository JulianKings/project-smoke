package com.smoke.communication.codecs;

import com.smoke.communication.protocol.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// @ChannelHandler.Sharable
public class MessageRequestDecoder extends ByteToMessageDecoder {

    // (sizeof(Integer) + sizeof(Short)) - 1 = 0x05
    private static final byte REQUEST_MIN_SIZE = 0x05;

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> output) throws Exception
    {
        // Are there enough bytes to process?
        if (buffer.readableBytes() > REQUEST_MIN_SIZE)
        {
            // Mark reader Index
            buffer.markReaderIndex();

            // Read message length
            int length = buffer.readInt();

            // Make sure there are enough bytes!
            if (buffer.readableBytes() < length)
            {
                buffer.resetReaderIndex(); return;
            }

            // Add a new POJO with the specified length
            output.add(new MessageRequest(buffer.readSlice(length).retain()));
        }
    }
}