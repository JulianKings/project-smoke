package com.smoke.communication.protocol;

import com.smoke.utilities.math.Numbers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class MessageResponse extends IMessage {

    private static final short PAYLOAD_SIZE_THRESHOLD = 0x100;

    // region #Methods
    @Override
    public void destruct()
    {
        this.iOPCode = 0;
        this.mBuffer = null;
    }

    public void writeByte(byte value)
    {
        this.mBuffer.writeByte(value);
    }

    public void writeLong(long value)
    {
        this.mBuffer.writeLong(value);
    }

    public void writeFloat(float value)
    {
        this.mBuffer.writeFloat(value);
    }

    public void writeShort(short value)
    {
        this.mBuffer.writeShort(value);
    }

    public void writeInteger(int value)
    {
        this.mBuffer.writeInt(value);
    }

    public void writeInteger(long value)
    {
        this.mBuffer.writeInt((int) value);
    }
    /*
    public void writeInt(int i)
    {
        this.mBuffer.writeInt(i);
    }
    */
    public void writeString(char value)
    {
        this.mBuffer.writeShort(Numbers.ONE);
        this.mBuffer.writeBytes(String.valueOf(value).getBytes(CharsetUtil.ISO_8859_1));
    }

    public void writeString(String value)
    {
        if (value == null)
        {
            this.mBuffer.writeShort(Numbers.ZERO);
        }

        else
        {
            this.mBuffer.writeShort(value.length());
            this.mBuffer.writeBytes(value.getBytes(CharsetUtil.ISO_8859_1));
        }
    }

    public void writeBoolean(boolean value)
    {
        this.mBuffer.writeBoolean(value);
    }
    /*
    public void writeBoolean(boolean b)
    {
        this.mBuffer.writeByte(b ? 1 : 0);
    }
    */
    // endregion

    // region #Accessors
    public ByteBuf getPayload()
    {
        this.mBuffer.setInt(0, (this.mBuffer.writerIndex() - 4));

        return this.mBuffer;
    }
    // endregion

    // region #Constructors
    public MessageResponse(short OPCode)
    {
        super(OPCode, Unpooled.directBuffer(
                MessageResponse.PAYLOAD_SIZE_THRESHOLD
        ));

        this.mBuffer.writeInt(0); this.mBuffer.writeShort(OPCode);
    }

    public MessageResponse(short OPCode, int caliper)
    {
        super(OPCode, Unpooled.directBuffer(
                MessageResponse.PAYLOAD_SIZE_THRESHOLD + caliper - 0x100
        ));

        this.mBuffer.writeInt(0); this.mBuffer.writeShort(OPCode);
    }
    // endregion
}