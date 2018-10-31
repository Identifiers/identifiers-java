package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.Assert;
import io.identifiers.semantic.Geo;
import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public final class GeoValueCodec implements ValueCodec<Geo> {

    private final ValueCodec<Double> floatCodec = new FloatValueCodec();

    @Override
    public Value encode(Geo value) {
        Value[] values = {
            floatCodec.encode(value.getLatitude()),
            floatCodec.encode(value.getLongitude())
        };
        return ValueFactory.newArray(values, true);
    }

    @Override
    public Geo decode(MessageUnpacker unpacker) throws IOException {
        int size = unpacker.unpackArrayHeader();
        Assert.state(size == 2, "Wrong array size. Expected 2, received %s", size);
        return new Geo(
            floatCodec.decode(unpacker),
            floatCodec.decode(unpacker)
        );
    }
}
