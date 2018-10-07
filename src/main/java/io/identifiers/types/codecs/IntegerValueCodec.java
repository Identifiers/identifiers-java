package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class IntegerValueCodec implements ValueCodec<Integer> {

    @Override
    public Value encode(Integer value) {
        return ValueFactory.newInteger(value);
    }

    @Override
    public Integer decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackInt();
    }
}
