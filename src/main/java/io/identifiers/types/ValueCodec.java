package io.identifiers.types;

import java.io.IOException;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

interface ValueCodec<T> {

    Value encode(T value);

    T decode(MessageUnpacker unpacker) throws IOException;
}
