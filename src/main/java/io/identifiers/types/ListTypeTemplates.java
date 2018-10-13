package io.identifiers.types;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

final class ListTypeTemplates {

    private ListTypeTemplates() {
        // static class
    }

    // todo: collapse all the template creation into a single TypeTemplateProvider class
    static TypeTemplate<List<String>> forStringList = createTemplate(IdentifierType.STRING_LIST, TypeTemplates.forString);

    static TypeTemplate<List<Boolean>> forBooleanList = createTemplate(IdentifierType.BOOLEAN_LIST, TypeTemplates.forBoolean);

    static TypeTemplate<List<Integer>> forIntegerList = createTemplate(IdentifierType.INTEGER_LIST, TypeTemplates.forInteger);

    static TypeTemplate<List<Double>> forFloatList = createTemplate(IdentifierType.FLOAT_LIST, TypeTemplates.forFloat);

    static TypeTemplate<List<Long>> forLongList = createTemplate(IdentifierType.LONG_LIST, TypeTemplates.forLong);

    static TypeTemplate<List<byte[]>> forBytesList = createTemplate(IdentifierType.BYTES_LIST, TypeTemplates.forBytes);

    @SuppressWarnings("unchecked")
    static TypeTemplate<List<Identifier<?>>> forCompositeList = new ImmutableValueListTypeTemplate(
        new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_LIST, ValueCodecProvider.getCodec(IdentifierType.COMPOSITE_LIST)));

    static TypeTemplate<List<UUID>> forUuidList = createTemplate(IdentifierType.UUID_LIST, TypeTemplates.forUuid);

    static TypeTemplate<List<Instant>> forDatetimeList = createTemplate(IdentifierType.DATETIME_LIST, TypeTemplates.forDatetime);


    private static <T> TypeTemplate<List<T>> createTemplate(IdentifierType type, TypeTemplate<T> valueTemplate) {
        IdentifierEncoder<List<T>> encoder = new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
        return new ListTypeTemplateWithEncoder<>(encoder, valueTemplate);
    }
}
