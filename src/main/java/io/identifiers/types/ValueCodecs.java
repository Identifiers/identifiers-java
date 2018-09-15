package io.identifiers.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

final class ValueCodecs {

    private ValueCodecs() {
        //static class
    }

    static final ValueCodec<String> stringCodec = new ValueCodec<String>() {
        @Override
        public Value encode(String value) {
            return ValueFactory.newString(value);
        }

        @Override
        public String decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackString();
        }
    };
    static final ValueCodec<List<String>> stringListCodec = createListCodec(stringCodec);
    static final ValueCodec<Map<String, String>> stringMapCodec = createMapCodec(stringCodec);

    static final ValueCodec<Boolean> booleanCodec = new ValueCodec<Boolean>() {
        @Override
        public Value encode(Boolean value) {
            return ValueFactory.newBoolean(value);
        }

        @Override
        public Boolean decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackBoolean();
        }
    };
    static final ValueCodec<List<Boolean>> booleanListCodec = createListCodec(booleanCodec);
    static final ValueCodec<Map<String, Boolean>> booleanMapCodec = createMapCodec(booleanCodec);

    static final ValueCodec<Integer> integerCodec = new ValueCodec<Integer>() {
        @Override
        public Value encode(Integer value) {
            return ValueFactory.newInteger(value);
        }

        @Override
        public Integer decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackInt();
        }
    };
    static final ValueCodec<List<Integer>> integerListCodec = createListCodec(integerCodec);
    static final ValueCodec<Map<String, Integer>> integerMapCodec = createMapCodec(integerCodec);

    static final ValueCodec<Float> floatCodec = new ValueCodec<Float>() {
        @Override
        public Value encode(Float value) {
            return ValueFactory.newFloat(value);
        }

        @Override
        public Float decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackFloat();
        }
    };
    static final ValueCodec<List<Float>> floatListCodec = createListCodec(floatCodec);
    static final ValueCodec<Map<String, Float>> floatMapCodec = createMapCodec(floatCodec);

    static final ValueCodec<Long> longCodec = new ValueCodec<Long>() {
        @Override
        public Value encode(Long value) {
            return ValueFactory.newInteger(value);
        }

        @Override
        public Long decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackLong();
        }
    };
    static final ValueCodec<List<Long>> longListCodec = createListCodec(longCodec);
    static final ValueCodec<Map<String, Long>> longMapCodec = createMapCodec(longCodec);

    static ValueCodec<byte[]> bytesCodec = new ValueCodec<byte[]>() {
        @Override
        public Value encode(byte[] bytes) {
            return ValueFactory.newBinary(bytes, true);
        }

        @Override
        public byte[] decode(MessageUnpacker unpacker) throws IOException {
            int size = unpacker.unpackBinaryHeader();
            byte[] bytes = new byte[size];
            unpacker.readPayload(bytes);
            return bytes;
        }
    };
    static final ValueCodec<List<byte[]>> bytesListCodec = createListCodec(bytesCodec);
    static final ValueCodec<Map<String, byte[]>> bytesMapCodec = createMapCodec(bytesCodec);


    private static <T> ValueCodec<List<T>> createListCodec(ValueCodec<T> valueCodec) {
        return new ValueCodec<List<T>>() {
            @Override
            public Value encode(List<T> values) {
                int size = values.size();
                Value[] encodedValues = new Value[size];
                IntStream.range(0, size)
                    .forEach((pos) -> encodedValues[pos] = valueCodec.encode(values.get(pos)));
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

    private static <T> ValueCodec<Map<String, T>> createMapCodec(ValueCodec<T> valueCodec) {
        return new ValueCodec<Map<String, T>>() {
            @Override
            public Value encode(final Map<String, T> valueMap) {
                Set<Map.Entry<String, T>> entries = valueMap.entrySet();
                Value[] encodedKVs = new Value[entries.size() * 2];
                int i = 0;
                for (Map.Entry<String, T> entry : entries) {
                    encodedKVs[i++] = ValueFactory.newString(entry.getKey());
                    encodedKVs[i++] = valueCodec.encode(entry.getValue());
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
