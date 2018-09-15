package io.identifiers.types;

import java.util.SortedMap;

import io.identifiers.IdentifierType;

final class MapTypeTemplates {

    private MapTypeTemplates() {
        // static class
    }

    static TypeTemplate<SortedMap<String, String>> forStringMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.STRING_MAP, ValueCodecs.stringCodec),
            TypeTemplates.forString);

    static TypeTemplate<SortedMap<String, Boolean>> forBooleanMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.BOOLEAN_MAP, ValueCodecs.booleanCodec),
            TypeTemplates.forBoolean);

    static TypeTemplate<SortedMap<String, Integer>> forIntegerMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.INTEGER_MAP, ValueCodecs.integerCodec),
            TypeTemplates.forInteger);

    static TypeTemplate<SortedMap<String, Float>> forFloatMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.FLOAT_MAP, ValueCodecs.floatCodec),
            TypeTemplates.forFloat);

    static TypeTemplate<SortedMap<String, Long>> forLongMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.LONG_MAP, ValueCodecs.longCodec),
            TypeTemplates.forLong);

    static TypeTemplate<SortedMap<String, byte[]>> forBytesMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.BYTES_MAP, ValueCodecs.bytesCodec),
            TypeTemplates.forBytes);
}
