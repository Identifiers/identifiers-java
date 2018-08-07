package io.identifiers.types;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;

class SingleIndentifierEncoder<T> extends AbstractIdentifierEncoder<T> {

    private final ValueCodec<T> codec;

    SingleIndentifierEncoder(IdentifierType type, ValueCodec<T> codec) {
        super(type);
        this.codec = codec;
    }

    @Override
    public Value encodeValue(T value) {
        return codec.encode(value);
    }
}
