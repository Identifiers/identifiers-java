package io.identifiers.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.identifiers.Assert;
import io.identifiers.Factory;
import io.identifiers.IdentifierType;
import io.identifiers.ListIdentifierFactory;
import io.identifiers.MapIdentifierFactory;
import io.identifiers.SingleIdentifierFactory;

final class IdentifierDecoderProvider {

    IdentifierDecoderProvider() {
        // static class
    }

    static IdentifierDecoder findDecoder(int typeCode) {
        IdentifierDecoder decoder = decoderMap.get(typeCode);
        Assert.argumentExists(decoder, "cannot unpack identifier type %s", typeCode);
        return decoder;
    }

    private static final Map<Integer, IdentifierDecoder> decoderMap = new HashMap<>();

    static {
        decoderMap.put(
            IdentifierType.STRING.code(), composeDecoder(
                ValueCodecs.stringCodec,
                Factory.forString));

        decoderMap.put(
            IdentifierType.STRING_LIST.code(), composeListDecoder(
                ValueCodecs.stringListCodec,
                Factory.forString));

        decoderMap.put(
            IdentifierType.STRING_MAP.code(), composeMapDecoder(
                ValueCodecs.stringMapCodec,
                Factory.forString));

        decoderMap.put(
            IdentifierType.BOOLEAN.code(), composeDecoder(
                ValueCodecs.booleanCodec,
                Factory.forBoolean));

        decoderMap.put(
            IdentifierType.BOOLEAN_LIST.code(), composeListDecoder(
                ValueCodecs.booleanListCodec,
                Factory.forBoolean));

        decoderMap.put(
            IdentifierType.BOOLEAN_MAP.code(), composeMapDecoder(
                ValueCodecs.booleanMapCodec,
                Factory.forBoolean));

        decoderMap.put(
            IdentifierType.INTEGER.code(), composeDecoder(
                ValueCodecs.integerCodec,
                Factory.forInteger));

        decoderMap.put(
            IdentifierType.INTEGER_LIST.code(), composeListDecoder(
                ValueCodecs.integerListCodec,
                Factory.forInteger));

        decoderMap.put(
            IdentifierType.INTEGER_MAP.code(), composeMapDecoder(
                ValueCodecs.integerMapCodec,
                Factory.forInteger));

        decoderMap.put(
            IdentifierType.FLOAT.code(), composeDecoder(
                ValueCodecs.floatCodec,
                Factory.forFloat));

        decoderMap.put(
            IdentifierType.FLOAT_LIST.code(), composeListDecoder(
                ValueCodecs.floatListCodec,
                Factory.forFloat));

        decoderMap.put(
            IdentifierType.FLOAT_MAP.code(), composeMapDecoder(
                ValueCodecs.floatMapCodec,
                Factory.forFloat));

        decoderMap.put(
            IdentifierType.LONG.code(), composeDecoder(
                ValueCodecs.longCodec,
                Factory.forLong));

        decoderMap.put(
            IdentifierType.LONG_LIST.code(), composeListDecoder(
                ValueCodecs.longListCodec,
                Factory.forLong));

        decoderMap.put(
            IdentifierType.LONG_MAP.code(), composeMapDecoder(
                ValueCodecs.longMapCodec,
                Factory.forLong));

        decoderMap.put(
            IdentifierType.BYTES.code(), composeDecoder(
                ValueCodecs.bytesCodec,
                Factory.forBytes));

        decoderMap.put(
            IdentifierType.BYTES_LIST.code(), composeListDecoder(
                ValueCodecs.bytesListCodec,
                Factory.forBytes));

        decoderMap.put(
            IdentifierType.BYTES_MAP.code(), composeMapDecoder(
                ValueCodecs.bytesMapCodec,
                Factory.forBytes));

        decoderMap.put(
            IdentifierType.COMPOSITE_LIST.code(), composeListDecoder(
                ValueCodecs.compositeListCodec,
                Factory.forComposite));

        decoderMap.put(
            IdentifierType.COMPOSITE_MAP.code(), composeMapDecoder(
                ValueCodecs.compositeMapCodec,
                Factory.forComposite));
    }

    private static <T> IdentifierDecoder composeDecoder(
            ValueCodec<T> codec,
            SingleIdentifierFactory<T> factory) {

        return (unpacker) -> factory.create(codec.decode(unpacker));
    }

    private static <T> IdentifierDecoder composeListDecoder(
            ValueCodec<List<T>> codec,
            ListIdentifierFactory<T> factory) {

        return (unpacker) -> factory.createList(codec.decode(unpacker));
    }

    private static <T> IdentifierDecoder composeMapDecoder(
            ValueCodec<Map<String, T>> codec,
            MapIdentifierFactory<T> factory) {

        return (unpacker) -> factory.createMap(codec.decode(unpacker));
    }
}
