package io.identifiers.types;

import io.identifiers.IdentifierType;

import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

/**
 * Provides TypeTemplate singletons for use in various places.
 */
final class TypeTemplates {

    private TypeTemplates() {
        // static class
    }

    static TypeTemplate<String> forString = new TypeTemplateImpl<>(
            IdentifierType.STRING,
            new IdentifierEncoder<String>(IdentifierType.STRING) {
                @Override
                Value encodeValue(final String value) {
                    return ValueFactory.newString(value);
                }
            });

    static TypeTemplate<Boolean> forBoolean = new TypeTemplateImpl<>(
            IdentifierType.BOOLEAN,
            new IdentifierEncoder<Boolean>(IdentifierType.BOOLEAN) {
                @Override
                Value encodeValue(final Boolean value) {
                    return ValueFactory.newBoolean(value);
                }
            });

    static TypeTemplate<Integer> forInteger = new TypeTemplateImpl<>(
            IdentifierType.INTEGER,
            new IdentifierEncoder<Integer>(IdentifierType.INTEGER) {
                @Override
                Value encodeValue(final Integer value) {
                    return ValueFactory.newInteger(value);
                }
            });

    static TypeTemplate<Float> forFloat = new TypeTemplateImpl<>(
            IdentifierType.FLOAT,
            new IdentifierEncoder<Float>(IdentifierType.FLOAT) {
                @Override
                Value encodeValue(final Float value) {
                    return ValueFactory.newFloat(value);
                }
            });

    static TypeTemplate<Long> forLong = new TypeTemplateImpl<>(
            IdentifierType.LONG,
            new IdentifierEncoder<Long>(IdentifierType.LONG) {
                @Override
                Value encodeValue(final Long value) {
                    return ValueFactory.newInteger(value);
                }
            });

    static TypeTemplate<byte[]> forBytes = new BytesTypeTemplate();
}
