package io.identifiers;

import static io.identifiers.types.IdentifierFactoryProvider.*;

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
    public static final IdentifierFactory<String> forString = createFactory(IdentifierType.STRING);

    /**
     * The {@link Boolean} identifier factory.
     */
    public static final IdentifierFactory<Boolean> forBoolean = createFactory(IdentifierType.BOOLEAN);

    /**
     * The {@link Integer} identifier factory.
     */
    public static final IdentifierFactory<Integer> forInteger = createFactory(IdentifierType.INTEGER);

    /**
     * The {@link Float} identifier factory.
     */
    public static final IdentifierFactory<Float> forFloat = createFactory(IdentifierType.FLOAT);

    /**
     * The {@link Long} identifier factory.
     */
    public static final IdentifierFactory<Long> forLong = createFactory(IdentifierType.LONG);

    /**
     * The byte array identifier factory.
     */
    public static final IdentifierFactory<byte[]> forBytes = createFactory(IdentifierType.BYTES);
}
