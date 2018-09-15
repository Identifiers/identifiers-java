package io.identifiers.types;

import java.util.Map;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

final class MapIdentifierEncoder<T> extends AbstractIdentifierEncoder<Map<String, T>> {

    private final ValueCodec<T> valueCodec;

    MapIdentifierEncoder(IdentifierType type, ValueCodec<T> valueCodec) {
        super(type);
        this.valueCodec = valueCodec;
    }

    @Override
    Value encodeValue(Map<String, T> map) {
        Value[] encodedKVs = new Value[map.size() * 2];
        int pos = 0;
        for (Map.Entry<String, T> entry : map.entrySet()) {
            encodedKVs[pos++] = ValueFactory.newString(entry.getKey());
            encodedKVs[pos++] = valueCodec.encode(entry.getValue());
        }
        return ValueFactory.newMap(encodedKVs, true);
    }
}
