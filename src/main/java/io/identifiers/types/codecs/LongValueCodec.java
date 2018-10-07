package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class LongValueCodec implements ValueCodec<Long> {

    @Override
    public Value encode(Long value) {
        return ValueFactory.newInteger(value);
    }

    @Override
    public Long decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackLong();
    }
}
