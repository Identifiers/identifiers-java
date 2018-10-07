package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class StringValueCodec implements ValueCodec<String> {

    @Override
    public Value encode(String value) {
        return ValueFactory.newString(value);
    }

    @Override
    public String decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackString();
    }
}
