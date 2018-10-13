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

        addCodec(IdentifierType.BOOLEAN, new BooleanValueCodec());
        addCodec(IdentifierType.BOOLEAN_LIST, createListCodec(IdentifierType.BOOLEAN));
        addCodec(IdentifierType.BOOLEAN_MAP, createMapCodec(IdentifierType.BOOLEAN));

        addCodec(IdentifierType.INTEGER, new IntegerValueCodec());
        addCodec(IdentifierType.INTEGER_LIST, createListCodec(IdentifierType.INTEGER));
        addCodec(IdentifierType.INTEGER_MAP, createMapCodec(IdentifierType.INTEGER));

        addCodec(IdentifierType.FLOAT, new FloatValueCodec());
        addCodec(IdentifierType.FLOAT_LIST, createListCodec(IdentifierType.FLOAT));
        addCodec(IdentifierType.FLOAT_MAP, createMapCodec(IdentifierType.FLOAT));

        addCodec(IdentifierType.LONG, new LongValueCodec());
        addCodec(IdentifierType.LONG_LIST, createListCodec(IdentifierType.LONG));
        addCodec(IdentifierType.LONG_MAP, createMapCodec(IdentifierType.LONG));

        addCodec(IdentifierType.BYTES, new BytesValueCodec());
        addCodec(IdentifierType.BYTES_LIST, createListCodec(IdentifierType.BYTES));
        addCodec(IdentifierType.BYTES_MAP, createMapCodec(IdentifierType.BYTES));

        ValueCodec<Identifier<?>> compositeCodec = new CompositeIdentifierValueCodec();
        addCodec(IdentifierType.COMPOSITE_LIST, createListCodec(compositeCodec));
        addCodec(IdentifierType.COMPOSITE_MAP, createMapCodec(compositeCodec));

        addCodec(IdentifierType.UUID, new UUIDValueCodec());
        addCodec(IdentifierType.UUID_LIST, createListCodec(IdentifierType.UUID));
        addCodec(IdentifierType.UUID_MAP, createMapCodec(IdentifierType.UUID));

        addCodec(IdentifierType.DATETIME, new DatetimeValueCodec());
        addCodec(IdentifierType.DATETIME_LIST, createListCodec(IdentifierType.DATETIME));
        addCodec(IdentifierType.DATETIME_MAP, createMapCodec(IdentifierType.DATETIME));
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