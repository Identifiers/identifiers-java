package io.identifiers.types;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

final class MapTypeTemplates {

    private MapTypeTemplates() {
        // static class
    }

    static TypeTemplate<Map<String, String>> forStringMap = createTemplate(IdentifierType.STRING_MAP, TypeTemplates.forString);

    static TypeTemplate<Map<String, Boolean>> forBooleanMap = createTemplate(IdentifierType.BOOLEAN_MAP, TypeTemplates.forBoolean);

    static TypeTemplate<Map<String, Integer>> forIntegerMap = createTemplate(IdentifierType.INTEGER_MAP, TypeTemplates.forInteger);

    static TypeTemplate<Map<String, Double>> forFloatMap = createTemplate(IdentifierType.FLOAT_MAP, TypeTemplates.forFloat);

    static TypeTemplate<Map<String, Long>> forLongMap = createTemplate(IdentifierType.LONG_MAP, TypeTemplates.forLong);

    static TypeTemplate<Map<String, byte[]>> forBytesMap = createTemplate(IdentifierType.BYTES_MAP, TypeTemplates.forBytes);

    @SuppressWarnings("unchecked")
    static TypeTemplate<Map<String, Identifier<?>>> forCompositeMap = new ImmutableValueMapTypeTemplate(
        new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_MAP, ValueCodecProvider.getCodec(IdentifierType.COMPOSITE_MAP)));

    static TypeTemplate<Map<String, UUID>> forUuidMap = createTemplate(IdentifierType.UUID_MAP, TypeTemplates.forUuid);

    static TypeTemplate<Map<String, Instant>> forDatetimeMap = createTemplate(IdentifierType.DATETIME_MAP, TypeTemplates.forDatetime);


    private static <T> TypeTemplate<Map<String, T>> createTemplate(IdentifierType type, TypeTemplate<T> valueTemplate) {
        IdentifierEncoder<Map<String, T>> encoder = new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
        return new MapTypeTemplateWithEncoder<>(encoder, valueTemplate);
    }
}
