package io.identifiers.types;

import io.identifiers.IdentifierType;

final class TypeTemplates {

    private TypeTemplates() {
        // static class
    }

    static TypeTemplate<String> forString = new SingleTypeTemplate<>(
        new SingleIndentifierEncoder<>(IdentifierType.STRING, ValueCodecs.stringCodec));

    static TypeTemplate<Boolean> forBoolean = new SingleTypeTemplate<>(
        new SingleIndentifierEncoder<>(IdentifierType.BOOLEAN, ValueCodecs.booleanCodec));

    static TypeTemplate<Integer> forInteger = new SingleTypeTemplate<>(
        new SingleIndentifierEncoder<>(IdentifierType.INTEGER, ValueCodecs.integerCodec));

    static TypeTemplate<Float> forFloat = new SingleTypeTemplate<>(
        new SingleIndentifierEncoder<>(IdentifierType.FLOAT, ValueCodecs.floatCodec));

    static TypeTemplate<Long> forLong = new SingleTypeTemplate<>(
        new SingleIndentifierEncoder<>(IdentifierType.LONG, ValueCodecs.longCodec));

    static TypeTemplate<byte[]> forBytes = new BytesTypeTemplate(
        new SingleIndentifierEncoder<>(IdentifierType.BYTES, ValueCodecs.bytesCodec));
}
