package io.identifiers.types;

import java.time.Instant;
import java.util.UUID;

import io.identifiers.IdentifierType;

final class TypeTemplates {

    private TypeTemplates() {
        // static class
    }

    static TypeTemplate<String> forString = createTypeTemplate(IdentifierType.STRING);

    static TypeTemplate<Boolean> forBoolean = createTypeTemplate(IdentifierType.BOOLEAN);

    static TypeTemplate<Integer> forInteger = createTypeTemplate(IdentifierType.INTEGER);

    static TypeTemplate<Double> forFloat = createTypeTemplate(IdentifierType.FLOAT);

    static TypeTemplate<Long> forLong = createTypeTemplate(IdentifierType.LONG);

    static TypeTemplate<byte[]> forBytes = new BytesTypeTemplate(createEncoder(IdentifierType.BYTES));

    static TypeTemplate<UUID> forUuid = createTypeTemplate(IdentifierType.UUID);

    static TypeTemplate<Instant> forDatetime = createTypeTemplate(IdentifierType.DATETIME);


    private static <T> TypeTemplate<T> createTypeTemplate(IdentifierType type) {
        return new TypeTemplateWithEncoder<>(createEncoder(type));
    }

    private static <T> IdentifierEncoder<T> createEncoder(IdentifierType type) {
        return new IdentifierEncoderWithCodec<>(type, ValueCodecProvider.getCodec(type));
    }
}
