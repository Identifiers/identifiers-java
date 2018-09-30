package io.identifiers.types;

import io.identifiers.IdentifierType;

final class TypeTemplates {

    private TypeTemplates() {
        // static class
    }

    static TypeTemplate<String> forString = new TypeTemplateWithEncoder<>(
        new IdentifierEncoderWithCodec<>(IdentifierType.STRING, ValueCodecs.stringCodec));

    static TypeTemplate<Boolean> forBoolean = new TypeTemplateWithEncoder<>(
        new IdentifierEncoderWithCodec<>(IdentifierType.BOOLEAN, ValueCodecs.booleanCodec));

    static TypeTemplate<Integer> forInteger = new TypeTemplateWithEncoder<>(
        new IdentifierEncoderWithCodec<>(IdentifierType.INTEGER, ValueCodecs.integerCodec));

    static TypeTemplate<Float> forFloat = new TypeTemplateWithEncoder<>(
        new IdentifierEncoderWithCodec<>(IdentifierType.FLOAT, ValueCodecs.floatCodec));

    static TypeTemplate<Long> forLong = new TypeTemplateWithEncoder<>(
        new IdentifierEncoderWithCodec<>(IdentifierType.LONG, ValueCodecs.longCodec));

    static TypeTemplate<byte[]> forBytes = new BytesTypeTemplate(
        new IdentifierEncoderWithCodec<>(IdentifierType.BYTES, ValueCodecs.bytesCodec));
}
