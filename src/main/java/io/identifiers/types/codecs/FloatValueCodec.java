package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class FloatValueCodec implements ValueCodec<Double> {
    @Override
    public Value encode(Double value) {
        return ValueFactory.newFloat(value);
    }

    @Override
    public Double decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackDouble();
    }
}
