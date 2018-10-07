package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class BytesValueCodec implements ValueCodec<byte[]> {

    @Override
    public Value encode(byte[] bytes) {
        return ValueFactory.newBinary(bytes, true);
    }

    @Override
    public byte[] decode(MessageUnpacker unpacker) throws IOException {
        int size = unpacker.unpackBinaryHeader();
        byte[] bytes = new byte[size];
        unpacker.readPayload(bytes);
        return bytes;
    }
}
