package io.identifiers.types;

import io.identifiers.IdentifierType;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.annotations.VisibleForTesting;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.impl.ImmutableArrayValueImpl;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

class TypeTemplateImpl<T> implements TypeTemplate<T> {

    private final IdentifierType type;
    private final Value typeCodeValue;
    private final Function<T, Value> encodeƒ;
    private final Function<T, Supplier<T>> valueSupplierƒ;


    private static <T> Function<T, Supplier<T>> unsupportedSupplier() {
        return (value) -> {
            throw new UnsupportedOperationException(String.format("no supplier available for %s", value));
        };
    }


    TypeTemplateImpl(
            IdentifierType type,
            Function<T, Value> encodeƒ) {

        this(type, encodeƒ, unsupportedSupplier());
    }


    TypeTemplateImpl(
            IdentifierType type,
            Function<T, Value> encodeƒ,
            Function<T, Supplier<T>> valueSupplierƒ) {

        this.type = type;
        this.typeCodeValue = ValueFactory.newInteger(type.code());
        this.encodeƒ = encodeƒ;
        this.valueSupplierƒ = valueSupplierƒ;
    }

    @Override
    public IdentifierType type() {
        return type;
    }

    @Override
    public Supplier<T> valueSupplier(T data) {
        return valueSupplierƒ.apply(data);
    }

    @Override
    public String toDataString(T value) {
        Value values = toValueArray(value);
        byte[] bytes = toBytes(values);
        return io.identifiers.base128.Encoder.encode(bytes);
    }

    @Override
    public String toHumanString(T value) {
        Value values = toValueArray(value);
        byte[] bytes = toBytes(values);
        return io.identifiers.base32.Encoder.encode(bytes);
    }

    @VisibleForTesting
    byte[] toBytes(Value values) {
        try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
            packer.packValue(values);
            return packer.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot msgPack %s", values), e);
        }
    }

    @VisibleForTesting
    Value toValueArray(T value) {
        return new ImmutableArrayValueImpl(new Value[] {
            typeCodeValue,
            encodeƒ.apply(value)
        });
    }
}
