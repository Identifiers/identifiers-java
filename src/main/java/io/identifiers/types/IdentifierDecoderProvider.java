package io.identifiers.types;

import static io.identifiers.IdentifierType.Modifiers.SEMANTIC_TYPE;

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

    /**
     * Mask to AND against a type code to find the non-semantic type code.
     */
    private static final int SEMANTIC_TYPE_FLAG = SEMANTIC_TYPE - 1;

    private static final Map<Integer, IdentifierDecoder> decoderMap = new HashMap<>();
    private static final Map<Integer, IdentifierType> identifierTypeMap = new HashMap<>();

    IdentifierDecoderProvider() {
        // static class
    }

    static IdentifierDecoder findDecoder(Integer typeCode) {
        IdentifierDecoder decoder = decoderMap.get(typeCode);
        if (decoder == null) {
            Assert.state(typeCode >= SEMANTIC_TYPE_FLAG, "No decoder for typeCode %s found.", typeCode);
            decoder = createUnknownCodec(typeCode);
        }

        Assert.argumentExists(decoder, "cannot unpack identifier type %s", typeCode);
        return decoder;
    }

    @SuppressWarnings("unchecked")
    private static IdentifierDecoder createUnknownCodec(Integer typeCode) {

        /*
            aspects of an UnknownIdentifier codec:
            1. Presents IdentifierType name: "unknown-basetype", code: unknownTypeCode
            2. Decoded value is the same values as base type--list, map, composite, including mutability management
            3. toString:  "ID<<unknown-base-type>>: base type value toString"
            4. toData/HumanString: [unknownTypeCode, encoded base value]
         */

        Integer baseTypeCode = typeCode & SEMANTIC_TYPE_FLAG;
        IdentifierType baseType = identifierTypeMap.get(baseTypeCode);
        Assert.argumentExists(baseType, "Cannot find base type code %d", baseTypeCode);

        // 1.
        String unknownName = String.format("unknown-%s", baseType.toString());
        IdentifierType unknownType = new IdentifierType(unknownName, typeCode);

        // 2, 3, 4: baseCodec will know how to decode/encode the value. It does not know about the identifier type
        ValueCodec<?> baseCodec = ValueCodecProvider.getCodec(baseType);
        IdentifierEncoder<?> unknownIdentifierEncoder = new IdentifierEncoderWithCodec<>(unknownType, baseCodec);
        TypeTemplate unknownTypeTemplate = new TypeTemplateWithEncoder(unknownIdentifierEncoder);

        IdentifierDecoder unknownDecoder = unpacker -> {
            Object decodedValue = baseCodec.decode(unpacker);
            return new ImmutableIdentifier<>(unknownTypeTemplate, decodedValue);
        };

        decoderMap.put(typeCode, unknownDecoder);
        return findDecoder(typeCode);
    }


    static {
        addItemDecoder(IdentifierType.STRING, Factory.forString);
        addListDecoder(IdentifierType.STRING_LIST, Factory.forString);
        addMapDecoder(IdentifierType.STRING_MAP, Factory.forString);

        addItemDecoder(IdentifierType.BOOLEAN, Factory.forBoolean);
        addListDecoder(IdentifierType.BOOLEAN_LIST, Factory.forBoolean);
        addMapDecoder(IdentifierType.BOOLEAN_MAP, Factory.forBoolean);

        addItemDecoder(IdentifierType.INTEGER, Factory.forInteger);
        addListDecoder(IdentifierType.INTEGER_LIST, Factory.forInteger);
        addMapDecoder(IdentifierType.INTEGER_MAP, Factory.forInteger);

        addItemDecoder(IdentifierType.FLOAT, Factory.forFloat);
        addListDecoder(IdentifierType.FLOAT_LIST, Factory.forFloat);
        addMapDecoder(IdentifierType.FLOAT_MAP, Factory.forFloat);

        addItemDecoder(IdentifierType.LONG, Factory.forLong);
        addListDecoder(IdentifierType.LONG_LIST, Factory.forLong);
        addMapDecoder(IdentifierType.LONG_MAP, Factory.forLong);

        addItemDecoder(IdentifierType.BYTES, Factory.forBytes);
        addListDecoder(IdentifierType.BYTES_LIST, Factory.forBytes);
        addMapDecoder(IdentifierType.BYTES_MAP, Factory.forBytes);

        addListDecoder(IdentifierType.COMPOSITE_LIST, Factory.forComposite);
        addMapDecoder(IdentifierType.COMPOSITE_MAP, Factory.forComposite);

        addItemDecoder(IdentifierType.UUID, Factory.forUuid);
        addListDecoder(IdentifierType.UUID_LIST, Factory.forUuid);
        addMapDecoder(IdentifierType.UUID_MAP, Factory.forUuid);

        addItemDecoder(IdentifierType.DATETIME, Factory.forDatetime);
        addListDecoder(IdentifierType.DATETIME_LIST, Factory.forDatetime);
        addMapDecoder(IdentifierType.DATETIME_MAP, Factory.forDatetime);
    }

    private static <T> void addItemDecoder(
            IdentifierType type,
            SingleIdentifierFactory<T> factory) {

        ValueCodec<T> codec = ValueCodecProvider.getCodec(type);
        addDecoder(type, composeDecoder(codec, factory));
    }

    private static <T> void addListDecoder(
            IdentifierType type,
            ListIdentifierFactory<T> factory) {

        ValueCodec<List<T>> codec = ValueCodecProvider.getCodec(type);
        addDecoder(type, composeListDecoder(codec, factory));
    }

    private static <T> void addMapDecoder(
            IdentifierType type,
            MapIdentifierFactory<T> factory) {

        ValueCodec<Map<String, T>> codec = ValueCodecProvider.getCodec(type);
        addDecoder(type, composeMapDecoder(codec, factory));
    }

    private static void addDecoder(IdentifierType type, IdentifierDecoder decoder) {
        identifierTypeMap.put(type.code(), type);
        decoderMap.put(type.code(), decoder);
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
