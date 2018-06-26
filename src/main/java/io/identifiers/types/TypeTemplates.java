package io.identifiers.types;

import io.identifiers.IdentifierType;
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
            ValueFactory::newString);

    static TypeTemplate<Boolean> forBoolean = new TypeTemplateImpl<>(
            IdentifierType.BOOLEAN,
            ValueFactory::newBoolean);

    static TypeTemplate<Integer> forInteger = new TypeTemplateImpl<>(
            IdentifierType.INTEGER,
            ValueFactory::newInteger);

    static TypeTemplate<Float> forFloat = new TypeTemplateImpl<>(
            IdentifierType.FLOAT,
            ValueFactory::newFloat);

    static TypeTemplate<Long> forLong = new TypeTemplateImpl<>(
            IdentifierType.LONG,
            ValueFactory::newInteger);

    static TypeTemplate<byte[]> forBytes = new TypeTemplateImpl<>(
            IdentifierType.BYTES,
            (bytes) -> ValueFactory.newBinary(bytes, true),
            (bytes) -> bytes::clone);
}
