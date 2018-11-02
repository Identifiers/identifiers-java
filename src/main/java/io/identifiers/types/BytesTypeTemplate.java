package io.identifiers.types;

import java.util.Arrays;

final class BytesTypeTemplate extends TypeTemplateWithEncoder<byte[]> {

    BytesTypeTemplate(IdentifierEncoder<byte[]> bytesEncoder) {
        super(bytesEncoder);
    }

    @Override
    public byte[] initialValue(byte[] value) {
        return value(value);
    }

    @Override
    public boolean isValueMutable() {
        return true;
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
