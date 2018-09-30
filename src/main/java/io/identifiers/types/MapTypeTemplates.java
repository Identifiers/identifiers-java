package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

final class MapTypeTemplates {

    private MapTypeTemplates() {
        // static class
    }

    static MapTypeTemplate<String> forStringMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.STRING_MAP, ValueCodecs.stringMapCodec),
            TypeTemplates.forString);

    static MapTypeTemplate<Boolean> forBooleanMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.BOOLEAN_MAP, ValueCodecs.booleanMapCodec),
            TypeTemplates.forBoolean);

    static MapTypeTemplate<Integer> forIntegerMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.INTEGER_MAP, ValueCodecs.integerMapCodec),
            TypeTemplates.forInteger);

    static MapTypeTemplate<Float> forFloatMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.FLOAT_MAP, ValueCodecs.floatMapCodec),
            TypeTemplates.forFloat);

    static MapTypeTemplate<Long> forLongMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.LONG_MAP, ValueCodecs.longMapCodec),
            TypeTemplates.forLong);

    static MapTypeTemplate<byte[]> forBytesMap = new MapTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.BYTES_MAP, ValueCodecs.bytesMapCodec),
            TypeTemplates.forBytes);

    @SuppressWarnings("unchecked")
    static MapTypeTemplate<Identifier<?>> forCompositeMap = new CompositeMapTypeTemplate(
            new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_MAP, ValueCodecs.compositeMapCodec));
}