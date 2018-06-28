package io.identifiers.types;

import java.io.IOException;
import java.util.function.Function;

import io.identifiers.IdentifierType;
import io.identifiers.base128.Base128Encoder;
import io.identifiers.base32.Base32Encoder;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.annotations.VisibleForTesting;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.impl.ImmutableArrayValueImpl;

final class IdentifierEncoder<T> {


    private final Value typeCodeValue;
    private final Function<T, Value> encodeƒ;

    IdentifierEncoder(
        IdentifierType type,
        Function<T, Value> encodeƒ) {

        this.typeCodeValue = ValueFactory.newInteger(type.code());
        this.encodeƒ = encodeƒ;
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


    @VisibleForTesting
    byte[] toBytes(Value values) {
        try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
            packer.packValue(values);
            return packer.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot msgPack %s", values), e);
        }
    }

    @VisibleForTesting
    Value toEncodingArray(T value) {
        return new ImmutableArrayValueImpl(new Value[] {
            typeCodeValue,
            encodeƒ.apply(value)
        });
    }

}
