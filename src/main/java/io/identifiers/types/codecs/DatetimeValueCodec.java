package io.identifiers.types.codecs;

import java.io.IOException;
import java.time.Instant;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

public class DatetimeValueCodec implements ValueCodec<Instant> {

    private final LongValueCodec longCodec = new LongValueCodec();

    @Override
    public Value encode(Instant value) {
        return longCodec.encode(value.toEpochMilli());
    }

    @Override
    public Instant decode(MessageUnpacker unpacker) throws IOException {
        long instant = longCodec.decode(unpacker);
        return Instant.ofEpochMilli(instant);
    }
}
