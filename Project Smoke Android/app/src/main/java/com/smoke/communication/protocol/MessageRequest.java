package com.smoke.communication.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class MessageRequest extends IMessage {

    // region #Methods
    @Override
    public void destruct()
    {
        this.mBuffer.release();

        this.iOPCode = 0;
        this.mBuffer = null;
    }

    public int readByte()
    {
        return this.mBuffer.readUnsignedByte();
    }

    public int readInteger()
    {
        return this.mBuffer.readInt();
    }

    public float readFloat()
    {
        return this.mBuffer.readFloat();
    }

    public short readShort()
    {
        return this.mBuffer.readShort();
    }

    public String readString()
    {
        return this.mBuffer.readSlice(this.readShort()).toString(CharsetUtil.ISO_8859_1);
    }

    public boolean readBoolean()
    {
        return (this.mBuffer.readBoolean());
    }

    public boolean  isReadable()
    {
        return this.mBuffer.isReadable();
    }
    // endregion

    // region #Accessors
    public short getOPCode()
    {
        return this.iOPCode;
    }
    // endregion

    // region #Constructors
    public MessageRequest(ByteBuf buffer)
    {
        super(buffer.readShort(), buffer);
    }
    // endregion
}