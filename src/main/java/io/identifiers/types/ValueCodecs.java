package io.identifiers.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.identifiers.Assert;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

/**
 * A ValueCodec contains an identifier type's knowledge of MessagePack.
 */
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

    static final ValueCodec<Double> floatCodec = new ValueCodec<Double>() {
        @Override
        public Value encode(Double value) {
            return ValueFactory.newFloat(value);
        }

        @Override
        public Double decode(MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackDouble();
        }
    };
    static final ValueCodec<List<Double>> floatListCodec = createListCodec(floatCodec);
    static final ValueCodec<Map<String, Double>> floatMapCodec = createMapCodec(floatCodec);

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

    private static final Map<IdentifierType, ValueCodec> codecs = new EnumMap<>(IdentifierType.class);
    static {
        codecs.put(IdentifierType.STRING, stringCodec);
        codecs.put(IdentifierType.STRING_LIST, stringListCodec);
        codecs.put(IdentifierType.STRING_MAP, stringMapCodec);

        codecs.put(IdentifierType.BOOLEAN, booleanCodec);
        codecs.put(IdentifierType.BOOLEAN_LIST, booleanListCodec);
        codecs.put(IdentifierType.BOOLEAN_MAP, booleanMapCodec);

        codecs.put(IdentifierType.INTEGER, integerCodec);
        codecs.put(IdentifierType.INTEGER_LIST, integerListCodec);
        codecs.put(IdentifierType.INTEGER_MAP, integerMapCodec);

        codecs.put(IdentifierType.FLOAT, floatCodec);
        codecs.put(IdentifierType.FLOAT_LIST, floatListCodec);
        codecs.put(IdentifierType.FLOAT_MAP, floatMapCodec);

        codecs.put(IdentifierType.LONG, longCodec);
        codecs.put(IdentifierType.LONG_LIST, longListCodec);
        codecs.put(IdentifierType.LONG_MAP, longMapCodec);

        codecs.put(IdentifierType.BYTES, bytesCodec);
        codecs.put(IdentifierType.BYTES_LIST, bytesListCodec);
        codecs.put(IdentifierType.BYTES_MAP, bytesMapCodec);
    }

    private static final ValueCodec<Identifier<?>> compositeCodec = new ValueCodec<Identifier<?>>() {
        @Override
        @SuppressWarnings("unchecked")
        public Value encode(final Identifier<?> value) {
            IdentifierType type = value.type();
            ValueCodec codec = codecs.get(type);
            Assert.argumentExists(codec, "No codec for type %s", type);
            /*
              This is a copy of logic in AbstractIdentifierEncoder but to share it would require some big holes pushed
              through Identifier, so copying is safer.
             */
            return ValueFactory.newArray(new Value[] {
                ValueFactory.newInteger(type.code()),
                codec.encode(value.value())
            }, true);
        }

        @Override
        public Identifier<?> decode(final MessageUnpacker unpacker) throws IOException {
            return IdentifierDecoders.unpackIdentifier(unpacker);
        }
    };

    static final ValueCodec<List<Identifier<?>>> compositeListCodec = createListCodec(compositeCodec);
    static final ValueCodec<Map<String, Identifier<?>>> compositeMapCodec = createMapCodec(compositeCodec);


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
