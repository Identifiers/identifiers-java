package io.identifiers.types;

import java.util.List;

import io.identifiers.IdentifierType;

final class ListTypeTemplates {

    private ListTypeTemplates() {
        // static class
    }

    static TypeTemplate<List<String>> forStringList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.STRING_LIST, ValueCodecs.stringCodec),
            TypeTemplates.forString);

    static TypeTemplate<List<Boolean>> forBooleanList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.BOOLEAN_LIST, ValueCodecs.booleanCodec),
            TypeTemplates.forBoolean);

    static TypeTemplate<List<Integer>> forIntegerList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.INTEGER_LIST, ValueCodecs.integerCodec),
            TypeTemplates.forInteger);

    static TypeTemplate<List<Float>> forFloatList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.FLOAT_LIST, ValueCodecs.floatCodec),
            TypeTemplates.forFloat);

    static TypeTemplate<List<Long>> forLongList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.LONG_LIST, ValueCodecs.longCodec),
            TypeTemplates.forLong);

    static TypeTemplate<List<byte[]>> forBytesList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.BYTES_LIST, ValueCodecs.bytesCodec),
            TypeTemplates.forBytes);
}
