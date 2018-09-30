package io.identifiers.types;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;

final class IdentifierEncoderWithCodec<T> extends AbstractIdentifierEncoder<T> {

    private final ValueCodec<T> codec;

    IdentifierEncoderWithCodec(IdentifierType type, ValueCodec<T> codec) {
        super(type);
        this.codec = codec;
    }

    @Override
    public Value encodeValue(T value) {
        return codec.encode(value);
    }
}
