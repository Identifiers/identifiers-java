package io.identifiers.types.codecs;

import java.io.IOException;

import io.identifiers.types.ValueCodec;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.impl.ImmutableDoubleValueImpl;

public class FloatValueCodec implements ValueCodec<Double> {
    @Override
    public Value encode(Double value) {
        return new ImmutableDoubleValueWithFloat(value);
    }

    @Override
    public Double decode(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackDouble();
    }

    private static class ImmutableDoubleValueWithFloat extends ImmutableDoubleValueImpl {

        ImmutableDoubleValueWithFloat(double value) {
            super(value);
        }

        @Override
        public void writeTo(MessagePacker pk) throws IOException {
            // ValueFactory always writes a float64; needs to be a float32 for single-precision
            if (toDouble() == toFloat()) {
                pk.packFloat(toFloat());
            } else {
                pk.packDouble(toDouble());
            }
        }
    }
}
