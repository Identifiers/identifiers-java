package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class BooleanValueCodec implements ValueCodec<Boolean> {

    @Override
    public Value encode(Boolean value) {
        return ValueFactory.newBoolean(value);
    }

    @Override
    public Boolean decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackBoolean();
    }
}
