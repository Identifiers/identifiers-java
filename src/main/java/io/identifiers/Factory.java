package io.identifiers;

import static io.identifiers.types.IdentifierDecoderSupport.decodeIdentifier;
import static io.identifiers.types.IdentifierFactoryProvider.compositeFactory;
import static io.identifiers.types.IdentifierFactoryProvider.typedFactory;

import java.time.Instant;
import java.util.UUID;

/**
 * Factory methods to create new {@link Identifier} instances.
 */
public final class Factory {

    private Factory() {
        // static class
    }

    /**
     * The {@link String} identifier factory.
     */
    public static final IdentifierFactory<String> forString = typedFactory(IdentifierType.STRING);

    /**
     * The {@link Boolean} identifier factory.
     */
    public static final IdentifierFactory<Boolean> forBoolean = typedFactory(IdentifierType.BOOLEAN);

    /**
     * The {@link Integer} identifier factory.
     */
    public static final IdentifierFactory<Integer> forInteger = typedFactory(IdentifierType.INTEGER);

    /**
     * The {@link Double} identifier factory.
     */
    public static final IdentifierFactory<Double> forFloat = typedFactory(IdentifierType.FLOAT);

    /**
     * The {@link Long} identifier factory.
     */
    public static final IdentifierFactory<Long> forLong = typedFactory(IdentifierType.LONG);

    /**
     * The byte array identifier factory.
     */
    public static final IdentifierFactory<byte[]> forBytes = typedFactory(IdentifierType.BYTES);

    /**
     * The composite identifier factory.
     */
    public static final CompositeIdentifierFactory forComposite = compositeFactory();

    /**
     * The UUID identifier factory.
     */
     public static final IdentifierFactory<UUID> forUuid = typedFactory(IdentifierType.UUID);

    /**
     * The Datetime identifier factory.
     */
     public static final IdentifierFactory<Instant> forDatetime = typedFactory(IdentifierType.DATETIME);


    /**
     * Parse an encoded identifier string into an Identifier instance.
     *
     * @param encodedString the encoded string
     * @return an Identifier instance
     * @throws IllegalArgumentException if the string is not an encoded identifier
     */
    public static <T> Identifier<T> decodeFromString(String encodedString) {
        return decodeIdentifier(encodedString);
    }
}
