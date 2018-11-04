package io.identifiers.types;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import io.identifiers.semantic.Geo;

final class TypeTemplates {

    private TypeTemplates() {
        // static class
    }

    static TypeTemplate<String> forString = createTypeTemplate(IdentifierType.STRING);
    static TypeTemplate<List<String>> forStringList = createListTemplate(IdentifierType.STRING_LIST, forString);
    static TypeTemplate<Map<String, String>> forStringMap = createMapTemplate(IdentifierType.STRING_MAP, forString);

    static TypeTemplate<Boolean> forBoolean = createTypeTemplate(IdentifierType.BOOLEAN);
    static TypeTemplate<List<Boolean>> forBooleanList = createListTemplate(IdentifierType.BOOLEAN_LIST, forBoolean);
    static TypeTemplate<Map<String, Boolean>> forBooleanMap = createMapTemplate(IdentifierType.BOOLEAN_MAP, forBoolean);

    static TypeTemplate<Integer> forInteger = createTypeTemplate(IdentifierType.INTEGER);
    static TypeTemplate<List<Integer>> forIntegerList = createListTemplate(IdentifierType.INTEGER_LIST, forInteger);
    static TypeTemplate<Map<String, Integer>> forIntegerMap = createMapTemplate(IdentifierType.INTEGER_MAP, forInteger);

    static TypeTemplate<Double> forFloat = createTypeTemplate(IdentifierType.FLOAT);
    static TypeTemplate<List<Double>> forFloatList = createListTemplate(IdentifierType.FLOAT_LIST, forFloat);
    static TypeTemplate<Map<String, Double>> forFloatMap = createMapTemplate(IdentifierType.FLOAT_MAP, forFloat);

    static TypeTemplate<Long> forLong = createTypeTemplate(IdentifierType.LONG);
    static TypeTemplate<List<Long>> forLongList = createListTemplate(IdentifierType.LONG_LIST, forLong);
    static TypeTemplate<Map<String, Long>> forLongMap = createMapTemplate(IdentifierType.LONG_MAP, forLong);

    static TypeTemplate<byte[]> forBytes = new BytesTypeTemplate(createEncoder(IdentifierType.BYTES));
    static TypeTemplate<List<byte[]>> forBytesList = createListTemplate(IdentifierType.BYTES_LIST, forBytes);
    static TypeTemplate<Map<String, byte[]>> forBytesMap = createMapTemplate(IdentifierType.BYTES_MAP, forBytes);

    @SuppressWarnings("unchecked")
    static TypeTemplate<List<Identifier<?>>> forCompositeList = new ImmutableValueListTypeTemplate(
            new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_LIST, ValueCodecProvider.getCodec(IdentifierType.COMPOSITE_LIST)));
    @SuppressWarnings("unchecked")
    static TypeTemplate<Map<String, Identifier<?>>> forCompositeMap = new ImmutableValueMapTypeTemplate(
            new IdentifierEncoderWithCodec(IdentifierType.COMPOSITE_MAP, ValueCodecProvider.getCodec(IdentifierType.COMPOSITE_MAP)));

    static TypeTemplate<UUID> forUuid = createTypeTemplate(IdentifierType.UUID);
    static TypeTemplate<List<UUID>> forUuidList = createListTemplate(IdentifierType.UUID_LIST, forUuid);
    static TypeTemplate<Map<String, UUID>> forUuidMap = createMapTemplate(IdentifierType.UUID_MAP, forUuid);

    static TypeTemplate<Instant> forDatetime = createTypeTemplate(IdentifierType.DATETIME);
    static TypeTemplate<List<Instant>> forDatetimeList = createListTemplate(IdentifierType.DATETIME_LIST, forDatetime);
    static TypeTemplate<Map<String, Instant>> forDatetimeMap = createMapTemplate(IdentifierType.DATETIME_MAP, forDatetime);

    static TypeTemplate<Geo> forGeo = createTypeTemplate(IdentifierType.GEO);
    static TypeTemplate<List<Geo>> forGeoList = createListTemplate(IdentifierType.GEO_LIST, forGeo);
    static TypeTemplate<Map<String, Geo>> forGeoMap = createMapTemplate(IdentifierType.GEO_MAP, forGeo);


    private static <T> TypeTemplate<T> createTypeTemplate(IdentifierType type) {
        return new TypeTemplateWithEncoder<>(createEncoder(type));
    }

    private static <T> IdentifierEncoder<T> createEncoder(IdentifierType type) {
        return new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
    }

    private static <T> TypeTemplate<List<T>> createListTemplate(IdentifierType type, TypeTemplate<T> valueTemplate) {
        IdentifierEncoder<List<T>> encoder = new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
        return new ListTypeTemplateWithEncoder<>(encoder, valueTemplate);
    }

    private static <T> TypeTemplate<Map<String, T>> createMapTemplate(IdentifierType type, TypeTemplate<T> valueTemplate) {
        IdentifierEncoder<Map<String, T>> encoder = new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
        return new MapTypeTemplateWithEncoder<>(encoder, valueTemplate);
    }
}
