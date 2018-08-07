package io.identifiers.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        public String decode(final MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackString();
        }
    };

    static final ValueCodec<List<String>> stringListCodec = createListCodec(stringCodec);

    static final ValueCodec<Boolean> booleanCodec = new ValueCodec<Boolean>() {
        @Override
        public Value encode(Boolean value) {
            return ValueFactory.newBoolean(value);
        }

        @Override
        public Boolean decode(final MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackBoolean();
        }
    };

    static final ValueCodec<List<Boolean>> booleanListCodec = createListCodec(booleanCodec);

    static final ValueCodec<Integer> integerCodec = new ValueCodec<Integer>() {
        @Override
        public Value encode(Integer value) {
            return ValueFactory.newInteger(value);
        }

        @Override
        public Integer decode(final MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackInt();
        }
    };

    static final ValueCodec<List<Integer>> integerListCodec = createListCodec(integerCodec);

    static final ValueCodec<Float> floatCodec = new ValueCodec<Float>() {
        @Override
        public Value encode(Float value) {
            return ValueFactory.newFloat(value);
        }

        @Override
        public Float decode(final MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackFloat();
        }
    };

    static final ValueCodec<List<Float>> floatListCodec = createListCodec(floatCodec);

    static final ValueCodec<Long> longCodec = new ValueCodec<Long>() {
        @Override
        public Value encode(Long value) {
            return ValueFactory.newInteger(value);
        }

        @Override
        public Long decode(final MessageUnpacker unpacker) throws IOException {
            return unpacker.unpackLong();
        }
    };

    static final ValueCodec<List<Long>> longListCodec = createListCodec(longCodec);

    static ValueCodec<byte[]> bytesCodec = new ValueCodec<byte[]>() {
        @Override
        public Value encode(byte[] bytes) {
            return ValueFactory.newBinary(bytes, true);
        }

        @Override
        public byte[] decode(final MessageUnpacker unpacker) throws IOException {
            int size = unpacker.unpackBinaryHeader();
            byte[] bytes = new byte[size];
            unpacker.readPayload(bytes);
            return bytes;
        }
    };

    static final ValueCodec<List<byte[]>> bytesListCodec = createListCodec(bytesCodec);


    private static <T> ValueCodec<List<T>> createListCodec(ValueCodec<T> valueCodec) {
        return new ValueCodec<List<T>>() {
            @Override
            public Value encode(final List<T> values) {
                int size = values.size();
                Value[] encodedValues = new Value[size];
                for (int i = 0; i < size; i++) {
                    encodedValues[i] = valueCodec.encode(values.get(i));
                }
                return ValueFactory.newArray(encodedValues, true);
            }

            @Override
            public List<T> decode(final MessageUnpacker unpacker) throws IOException {
                int size = unpacker.unpackArrayHeader();
                List<T> values = new ArrayList<>(size);
                while (size-- > 0) {
                    values.add(valueCodec.decode(unpacker));
                }
                return values;
            }
        };
    }
}
