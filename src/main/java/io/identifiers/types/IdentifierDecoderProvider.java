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
        addItemDecoder(IdentifierType.STRING, ValueCodecs.stringCodec,Factory.forString);
        addListDecoder(IdentifierType.STRING_LIST, ValueCodecs.stringListCodec, Factory.forString);
        addMapDecoder(IdentifierType.STRING_MAP, ValueCodecs.stringMapCodec, Factory.forString);

        addItemDecoder(IdentifierType.BOOLEAN, ValueCodecs.booleanCodec, Factory.forBoolean);
        addListDecoder(IdentifierType.BOOLEAN_LIST, ValueCodecs.booleanListCodec, Factory.forBoolean);
        addMapDecoder(IdentifierType.BOOLEAN_MAP, ValueCodecs.booleanMapCodec, Factory.forBoolean);

        addItemDecoder(IdentifierType.INTEGER, ValueCodecs.integerCodec, Factory.forInteger);
        addListDecoder(IdentifierType.INTEGER_LIST, ValueCodecs.integerListCodec, Factory.forInteger);
        addMapDecoder(IdentifierType.INTEGER_MAP, ValueCodecs.integerMapCodec, Factory.forInteger);

        addItemDecoder(IdentifierType.FLOAT, ValueCodecs.floatCodec, Factory.forFloat);
        addListDecoder(IdentifierType.FLOAT_LIST, ValueCodecs.floatListCodec, Factory.forFloat);
        addMapDecoder(IdentifierType.FLOAT_MAP, ValueCodecs.floatMapCodec, Factory.forFloat);

        addItemDecoder(IdentifierType.LONG, ValueCodecs.longCodec, Factory.forLong);
        addListDecoder(IdentifierType.LONG_LIST, ValueCodecs.longListCodec, Factory.forLong);
        addMapDecoder(IdentifierType.LONG_MAP, ValueCodecs.longMapCodec, Factory.forLong);

        addItemDecoder(IdentifierType.BYTES, ValueCodecs.bytesCodec, Factory.forBytes);
        addListDecoder(IdentifierType.BYTES_LIST, ValueCodecs.bytesListCodec, Factory.forBytes);
        addMapDecoder(IdentifierType.BYTES_MAP, ValueCodecs.bytesMapCodec, Factory.forBytes);

        addListDecoder(IdentifierType.COMPOSITE_LIST, ValueCodecs.compositeListCodec, Factory.forComposite);
        addMapDecoder(IdentifierType.COMPOSITE_MAP, ValueCodecs.compositeMapCodec, Factory.forComposite);

        addItemDecoder(IdentifierType.UUID, ValueCodecs.uuidCodec, Factory.forUuid);
        addListDecoder(IdentifierType.UUID_LIST, ValueCodecs.uuidListCodec, Factory.forUuid);
        addMapDecoder(IdentifierType.UUID_MAP, ValueCodecs.uuidMapCodec, Factory.forUuid);

        addItemDecoder(IdentifierType.DATETIME, ValueCodecs.datetimeCodec, Factory.forDatetime);
        addListDecoder(IdentifierType.DATETIME_LIST, ValueCodecs.datetimeListCodec, Factory.forDatetime);
        addMapDecoder(IdentifierType.DATETIME_MAP, ValueCodecs.datetimeMapCodec, Factory.forDatetime);
    }

    private static <T> void addItemDecoder(
            IdentifierType type,
            ValueCodec<T> codec,
            SingleIdentifierFactory<T> factory) {

        decoderMap.put(type.code(), composeDecoder(codec, factory));
    }

    private static <T> void addListDecoder(
            IdentifierType type,
            ValueCodec<List<T>> codec,
            ListIdentifierFactory<T> factory) {

        decoderMap.put(type.code(), composeListDecoder(codec, factory));
    }

    private static <T> void addMapDecoder(
            IdentifierType type,
            ValueCodec<Map<String, T>> codec,
            MapIdentifierFactory<T> factory) {

        decoderMap.put(type.code(), composeMapDecoder(codec, factory));
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
