package io.identifiers.types;

import java.io.IOException;

import io.identifiers.IdentifierType;
import io.identifiers.base128.Base128Encoder;
import io.identifiers.base32.Base32Encoder;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

abstract class AbstractIdentifierEncoder<T> implements IdentifierEncoder<T> {

    private static final int BUFFER_SIZE = 2048;

    private static final MessagePack.PackerConfig PACKER = new MessagePack.PackerConfig()
        .withBufferSize(BUFFER_SIZE)
        .withSmallStringOptimizationThreshold(BUFFER_SIZE);

    private final IdentifierType type;
    private final Value typeCodeValue;

    AbstractIdentifierEncoder(IdentifierType type) {
        this.type = type;
        typeCodeValue = ValueFactory.newInteger(type.code());
    }

    abstract Value encodeValue(T value);

    @Override
    public IdentifierType getType() {
        return type;
    }

    @Override
    public String toDataString(T value) {
        Value values = toEncodingArray(value);
        byte[] bytes = toBytes(values);
        return Base128Encoder.encode(bytes);
    }

    @Override
    public String toHumanString(T value) {
        Value values = toEncodingArray(value);
        byte[] bytes = toBytes(values);
        return Base32Encoder.encode(bytes);
    }

    private byte[] toBytes(Value values) {
        try (MessageBufferPacker packer = PACKER.newBufferPacker()) {
            packer.packValue(values);
            return packer.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot msgPack %s", values), e);
        }
    }

    private Value toEncodingArray(T value) {
        return ValueFactory.newArray(new Value[] {
            typeCodeValue,
            encodeValue(value)
        }, true);
    }
}
