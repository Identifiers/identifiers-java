package io.identifiers;

import io.identifiers.types.ImmutableIdentifierFactory;
import io.identifiers.types.SupplyingValueIdentifierFactory;
import io.identifiers.types.TypeTemplates;

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
    public static final IdentifierFactory<String> forString = new ImmutableIdentifierFactory<>(TypeTemplates.forString);

    /**
     * The {@link Boolean} identifier factory.
     */
    public static final IdentifierFactory<Boolean> forBoolean = new ImmutableIdentifierFactory<>(TypeTemplates.forBoolean);

    /**
     * The {@link Integer} identifier factory.
     */
    public static final IdentifierFactory<Integer> forInteger = new ImmutableIdentifierFactory<>(TypeTemplates.forInteger);

    /**
     * The {@link Float} identifier factory.
     */
    public static final IdentifierFactory<Float> forFloat = new ImmutableIdentifierFactory<>(TypeTemplates.forFloat);

    /**
     * The {@link Long} identifier factory.
     */
    public static final IdentifierFactory<Long> forLong = new ImmutableIdentifierFactory<>(TypeTemplates.forLong);

    /**
     * The byte array identifier factory.
     */
    public static final IdentifierFactory<byte[]> forBytes = new SupplyingValueIdentifierFactory<>(TypeTemplates.forBytes);
}
