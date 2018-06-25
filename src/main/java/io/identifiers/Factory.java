package io.identifiers;

import io.identifiers.types.ImmutableIdentifierFactory;
import io.identifiers.types.SupplyingValueIdentifierFactory;
import io.identifiers.types.TypeTemplates;

public final class Factory {

    private Factory() {
        // static class
    }

    public static final IdentifierFactory<String> forString = new ImmutableIdentifierFactory<>(TypeTemplates.forString);

    public static final IdentifierFactory<Boolean> forBoolean = new ImmutableIdentifierFactory<>(TypeTemplates.forBoolean);

    public static final IdentifierFactory<Integer> forInteger = new ImmutableIdentifierFactory<>(TypeTemplates.forInteger);

    public static final IdentifierFactory<Float> forFloat = new ImmutableIdentifierFactory<>(TypeTemplates.forFloat);

    public static final IdentifierFactory<Long> forLong = new ImmutableIdentifierFactory<>(TypeTemplates.forLong);

    public static final IdentifierFactory<byte[]> forBytes = new SupplyingValueIdentifierFactory<>(TypeTemplates.forBytes);
}
