package io.identifiers.types;

import java.util.List;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;


class ListIdentifierEncoder<T> extends AbstractIdentifierEncoder<List<T>> {

    private final ValueCodec<T> valueCodec;

    ListIdentifierEncoder(IdentifierType type, ValueCodec<T> valueCodec) {
        super(type);
        this.valueCodec = valueCodec;
    }

    @Override
    public Value encodeValue(List<T> values) {
        Value[] encodedValues = new Value[values.size()];
        for (int i = 0; i < encodedValues.length; i++) {
            encodedValues[i] = valueCodec.encode(values.get(i));
        }
        return ValueFactory.newArray(encodedValues, true);
    }
}
