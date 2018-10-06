package io.identifiers;

public final class TypeCodeModifiers {

    private TypeCodeModifiers() {
        // static class
    }

    /**
     * Mask to AND against a type code to find the non-semantic type code.
     */
    public static final short SEMANTIC_TYPE_MASK = IdentifierType.SEMANTIC_TYPE - 1;
}
