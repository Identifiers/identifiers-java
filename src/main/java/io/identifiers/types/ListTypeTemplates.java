package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

final class ListTypeTemplates {

    private ListTypeTemplates() {
        // static class
    }

    static ListTypeTemplate<String> forStringList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.STRING_LIST, ValueCodecs.stringListCodec),
            TypeTemplates.forString);

    static ListTypeTemplate<Boolean> forBooleanList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.BOOLEAN_LIST, ValueCodecs.booleanListCodec),
            TypeTemplates.forBoolean);

    static ListTypeTemplate<Integer> forIntegerList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.INTEGER_LIST, ValueCodecs.integerListCodec),
            TypeTemplates.forInteger);

    static ListTypeTemplate<Float> forFloatList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.FLOAT_LIST, ValueCodecs.floatListCodec),
            TypeTemplates.forFloat);

    static ListTypeTemplate<Long> forLongList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.LONG_LIST, ValueCodecs.longListCodec),
            TypeTemplates.forLong);

    static ListTypeTemplate<byte[]> forBytesList = new ListTypeTemplateWithEncoder<>(
            new IdentifierEncoderWithCodec<>(IdentifierType.BYTES_LIST, ValueCodecs.bytesListCodec),
            TypeTemplates.forBytes);

    @SuppressWarnings("unchecked")
    static ListTypeTemplate<Identifier<?>> forCompositeList = new CompositeListTypeTemplate(
            new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_LIST, ValueCodecs.compositeListCodec));
}
