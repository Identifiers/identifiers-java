package io.identifiers.types;

import io.identifiers.IdentifierType;
import org.msgpack.value.ValueFactory;

public class TypeTemplates {

    public static TypeTemplate<String> forString = new TypeTemplateImpl<>(
            IdentifierType.STRING,
            ValueFactory::newString);

    public static TypeTemplate<Boolean> forBoolean = new TypeTemplateImpl<>(
            IdentifierType.BOOLEAN,
            ValueFactory::newBoolean);

    public static TypeTemplate<Integer> forInteger = new TypeTemplateImpl<>(
            IdentifierType.INTEGER,
            ValueFactory::newInteger);

    public static TypeTemplate<Float> forFloat = new TypeTemplateImpl<>(
            IdentifierType.FLOAT,
            ValueFactory::newFloat);

    public static TypeTemplate<Long> forLong = new TypeTemplateImpl<>(
            IdentifierType.LONG,
            ValueFactory::newInteger);

    public static TypeTemplate<byte[]> forBytes = new TypeTemplateImpl<>(
            IdentifierType.BYTES,
            ValueFactory::newBinary,
            (bytes) -> bytes::clone);
}
