package io.identifiers.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import io.identifiers.types.codecs.GeoValueCodec;
import io.identifiers.types.codecs.UUIDValueCodec;
import io.identifiers.types.codecs.BooleanValueCodec;
import io.identifiers.types.codecs.BytesValueCodec;
import io.identifiers.types.codecs.DatetimeValueCodec;
import io.identifiers.types.codecs.FloatValueCodec;
import io.identifiers.types.codecs.IntegerValueCodec;
import io.identifiers.types.codecs.LongValueCodec;
import io.identifiers.types.codecs.StringValueCodec;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

final class ValueCodecProvider {

    private ValueCodecProvider() {
        //static class
    }


    private static final Map<Integer, ValueCodec> codecs = new HashMap<>();

    static {
        addCodec(IdentifierType.STRING, new StringValueCodec());
        addCodec(IdentifierType.STRING_LIST, createListCodec(IdentifierType.STRING));
        addCodec(IdentifierType.STRING_MAP, createMapCodec(IdentifierType.STRING));
        addCodec(IdentifierType.STRING_LIST_LIST, createListCodec(IdentifierType.STRING_LIST));
        addCodec(IdentifierType.STRING_MAP_LIST, createListCodec(IdentifierType.STRING_MAP));
        addCodec(IdentifierType.STRING_LIST_MAP, createMapCodec(IdentifierType.STRING_LIST));
        addCodec(IdentifierType.STRING_MAP_MAP, createMapCodec(IdentifierType.STRING_MAP));

        addCodec(IdentifierType.BOOLEAN, new BooleanValueCodec());
        addCodec(IdentifierType.BOOLEAN_LIST, createListCodec(IdentifierType.BOOLEAN));
        addCodec(IdentifierType.BOOLEAN_MAP, createMapCodec(IdentifierType.BOOLEAN));
        addCodec(IdentifierType.BOOLEAN_LIST_LIST, createListCodec(IdentifierType.BOOLEAN_LIST));
        addCodec(IdentifierType.BOOLEAN_MAP_LIST, createListCodec(IdentifierType.BOOLEAN_MAP));
        addCodec(IdentifierType.BOOLEAN_LIST_MAP, createMapCodec(IdentifierType.BOOLEAN_LIST));
        addCodec(IdentifierType.BOOLEAN_MAP_MAP, createMapCodec(IdentifierType.BOOLEAN_MAP));

        addCodec(IdentifierType.INTEGER, new IntegerValueCodec());
        addCodec(IdentifierType.INTEGER_LIST, createListCodec(IdentifierType.INTEGER));
        addCodec(IdentifierType.INTEGER_MAP, createMapCodec(IdentifierType.INTEGER));
        addCodec(IdentifierType.INTEGER_LIST_LIST, createListCodec(IdentifierType.INTEGER_LIST));
        addCodec(IdentifierType.INTEGER_MAP_LIST, createListCodec(IdentifierType.INTEGER_MAP));
        addCodec(IdentifierType.INTEGER_LIST_MAP, createMapCodec(IdentifierType.INTEGER_LIST));
        addCodec(IdentifierType.INTEGER_MAP_MAP, createMapCodec(IdentifierType.INTEGER_MAP));

        addCodec(IdentifierType.FLOAT, new FloatValueCodec());
        addCodec(IdentifierType.FLOAT_LIST, createListCodec(IdentifierType.FLOAT));
        addCodec(IdentifierType.FLOAT_MAP, createMapCodec(IdentifierType.FLOAT));
        addCodec(IdentifierType.FLOAT_LIST_LIST, createListCodec(IdentifierType.FLOAT_LIST));
        addCodec(IdentifierType.FLOAT_MAP_LIST, createListCodec(IdentifierType.FLOAT_MAP));
        addCodec(IdentifierType.FLOAT_LIST_MAP, createMapCodec(IdentifierType.FLOAT_LIST));
        addCodec(IdentifierType.FLOAT_MAP_MAP, createMapCodec(IdentifierType.FLOAT_MAP));

        addCodec(IdentifierType.LONG, new LongValueCodec());
        addCodec(IdentifierType.LONG_LIST, createListCodec(IdentifierType.LONG));
        addCodec(IdentifierType.LONG_MAP, createMapCodec(IdentifierType.LONG));
        addCodec(IdentifierType.LONG_LIST_LIST, createListCodec(IdentifierType.LONG_LIST));
        addCodec(IdentifierType.LONG_MAP_LIST, createListCodec(IdentifierType.LONG_MAP));
        addCodec(IdentifierType.LONG_LIST_MAP, createMapCodec(IdentifierType.LONG_LIST));
        addCodec(IdentifierType.LONG_MAP_MAP, createMapCodec(IdentifierType.LONG_MAP));

        addCodec(IdentifierType.BYTES, new BytesValueCodec());
        addCodec(IdentifierType.BYTES_LIST, createListCodec(IdentifierType.BYTES));
        addCodec(IdentifierType.BYTES_MAP, createMapCodec(IdentifierType.BYTES));
        addCodec(IdentifierType.BYTES_LIST_LIST, createListCodec(IdentifierType.BYTES_LIST));
        addCodec(IdentifierType.BYTES_MAP_LIST, createListCodec(IdentifierType.BYTES_MAP));
        addCodec(IdentifierType.BYTES_LIST_MAP, createMapCodec(IdentifierType.BYTES_LIST));
        addCodec(IdentifierType.BYTES_MAP_MAP, createMapCodec(IdentifierType.BYTES_MAP));

        ValueCodec<Identifier<?>> compositeCodec = new CompositeIdentifierValueCodec();
        addCodec(IdentifierType.COMPOSITE_LIST, createListCodec(compositeCodec));
        addCodec(IdentifierType.COMPOSITE_MAP, createMapCodec(compositeCodec));

        addCodec(IdentifierType.UUID, new UUIDValueCodec());
        addCodec(IdentifierType.UUID_LIST, createListCodec(IdentifierType.UUID));
        addCodec(IdentifierType.UUID_MAP, createMapCodec(IdentifierType.UUID));

        addCodec(IdentifierType.DATETIME, new DatetimeValueCodec());
        addCodec(IdentifierType.DATETIME_LIST, createListCodec(IdentifierType.DATETIME));
        addCodec(IdentifierType.DATETIME_MAP, createMapCodec(IdentifierType.DATETIME));

        addCodec(IdentifierType.GEO, new GeoValueCodec());
        addCodec(IdentifierType.GEO_LIST, createListCodec(IdentifierType.GEO));
        addCodec(IdentifierType.GEO_MAP, createMapCodec(IdentifierType.GEO));
    }

    static <T> ValueCodec<T> getCodec(IdentifierType type) {
        return getCodec(type.code());
    }

    @SuppressWarnings("unchecked")
    private static <T> ValueCodec<T> getCodec(Integer typeCode) {
        return codecs.get(typeCode);
    }

    private static void addCodec(IdentifierType type, ValueCodec codec) {
        codecs.put(type.code(), codec);
    }

    private static <T> ValueCodec<List<T>> createListCodec(IdentifierType type) {
        ValueCodec<T> valueCodec = getCodec(type);
        return createListCodec(valueCodec);
    }

    private static <T> ValueCodec<List<T>> createListCodec(ValueCodec<T> valueCodec) {
        return new ValueCodec<List<T>>() {
            @Override
            public Value encode(List<T> values) {
                int size = values.size();
                Value[] encodedValues = new Value[size];
                Arrays.setAll(encodedValues, (pos) -> valueCodec.encode(values.get(pos)));
                return ValueFactory.newArray(encodedValues, true);
            }

            @Override
            public List<T> decode(MessageUnpacker unpacker) throws IOException {
                int size = unpacker.unpackArrayHeader();
                List<T> values = new ArrayList<>(size);
                while (size-- > 0) {
                    values.add(valueCodec.decode(unpacker));
                }
                return values;
            }
        };
    }

    private static <T> ValueCodec<Map<String, T>> createMapCodec(IdentifierType type) {
        ValueCodec<T> valueCodec = getCodec(type);
        return createMapCodec(valueCodec);
    }

    private static <T> ValueCodec<Map<String, T>> createMapCodec(ValueCodec<T> valueCodec) {
        return new ValueCodec<Map<String, T>>() {
            @Override
            public Value encode(final Map<String, T> valueMap) {
                Set<Map.Entry<String, T>> entries = valueMap.entrySet();
                Value[] encodedKVs = new Value[entries.size() * 2];

                int pos = 0;
                for (Map.Entry<String, T> entry : entries) {
                    encodedKVs[pos++] = ValueFactory.newString(entry.getKey());
                    encodedKVs[pos++] = valueCodec.encode(entry.getValue());
                }
                return ValueFactory.newMap(encodedKVs, true);
            }

            @Override
            public Map<String, T> decode(final MessageUnpacker unpacker) throws IOException {
                int size = unpacker.unpackMapHeader();
                Map<String, T> valueMap = new TreeMap<>();
                while (size-- > 0) {
                    String key = unpacker.unpackString();
                    T value = valueCodec.decode(unpacker);
                    valueMap.put(key, value);
                }
                return valueMap;
            }
        };
    }
}
