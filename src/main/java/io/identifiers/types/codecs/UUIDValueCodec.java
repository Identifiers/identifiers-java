package io.identifiers.types.codecs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

import io.identifiers.Assert;
import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

public final class UUIDValueCodec implements ValueCodec<UUID> {

    private static final int BYTES_LENGTH = 16;

    private final ValueCodec<byte[]> bytesCodec = new BytesValueCodec();

    @Override
    public Value encode(UUID value) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[BYTES_LENGTH]);
        bb.putLong(value.getMostSignificantBits());
        bb.putLong(value.getLeastSignificantBits());
        return bytesCodec.encode(bb.array());
    }

    @Override
    public UUID decode(MessageUnpacker unpacker) throws IOException {
        byte[] bytes = bytesCodec.decode(unpacker);
        Assert.state(bytes.length == BYTES_LENGTH, "uuid bytes must be 16 bytes in lengthm but found %s", bytes.length);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }
}
