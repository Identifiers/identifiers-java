package io.identifiers.types;

import java.util.Arrays;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public final class BytesTypeTemplate extends TypeTemplateImpl<byte[]> {

    BytesTypeTemplate() {
        super(
            IdentifierType.BYTES,
            new IdentifierEncoder<byte[]>(IdentifierType.BYTES) {
                @Override
                Value encodeValue(final byte[] bytes) {
                    return ValueFactory.newBinary(bytes, true);
                }
            });
    }

    @Override
    public byte[] value(byte[] value) {
        return value.clone();
    }

    @Override
    public String valueString(byte[] value) {
        return Arrays.toString(value);
    }

    @Override
    public int valueHashCode(byte[] value) {
        return Arrays.hashCode(value);
    }

    @Override
    public boolean valuesEqual(byte[] v1, byte[] v2) {
        return Arrays.equals(v1, v2);
    }
}
