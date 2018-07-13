package io.identifiers.types;

import java.io.IOException;

import io.identifiers.IdentifierType;
import io.identifiers.base128.Base128Encoder;
import io.identifiers.base32.Base32Encoder;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.impl.ImmutableArrayValueImpl;

abstract class IdentifierEncoder<T> {

    private final Value typeCodeValue;

    IdentifierEncoder(IdentifierType type) {
        typeCodeValue = ValueFactory.newInteger(type.code());
    }

    String toDataString(T value) {
        Value values = toEncodingArray(value);
        byte[] bytes = toBytes(values);
        return Base128Encoder.encode(bytes);
    }

    String toHumanString(T value) {
        Value values = toEncodingArray(value);
        byte[] bytes = toBytes(values);
        return Base32Encoder.encode(bytes);
    }

    abstract Value encodeValue(T value);

    private byte[] toBytes(Value values) {
        try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
            packer.packValue(values);
            return packer.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot msgPack %s", values), e);
        }
    }

    private Value toEncodingArray(T value) {
        return new ImmutableArrayValueImpl(new Value[] {
            typeCodeValue,
            encodeValue(value)
        });
    }
}
