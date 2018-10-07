package io.identifiers.types;

import java.io.IOException;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

/**
 * A ValueCodec contains an identifier type's knowledge of MessagePack.
 */
public interface ValueCodec<T> {

    Value encode(T value);

    T decode(MessageUnpacker unpacker) throws IOException;
}
