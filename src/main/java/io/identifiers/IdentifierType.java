package io.identifiers;

public enum IdentifierType {

    STRING(0x00),
    BOOLEAN(0x01),
    INTEGER(0x02),
    FLOAT(0x03),
    LONG(0x04),
    BYTES(0x05),

    STRING_LIST(calculateListTypeCode(STRING.code)),
    BOOLEAN_LIST(calculateListTypeCode(BOOLEAN.code)),
    INTEGER_LIST(calculateListTypeCode(INTEGER.code)),
    FLOAT_LIST(calculateListTypeCode(FLOAT.code)),
    LONG_LIST(calculateListTypeCode(LONG.code)),
    BYTES_LIST(calculateListTypeCode(BYTES.code)),

    STRING_MAP(calculateMapTypeCode(STRING.code)),
    BOOLEAN_MAP(calculateMapTypeCode(BOOLEAN.code)),
    INTEGER_MAP(calculateMapTypeCode(INTEGER.code)),
    FLOAT_MAP(calculateMapTypeCode(FLOAT.code)),
    LONG_MAP(calculateMapTypeCode(LONG.code)),
    BYTES_MAP(calculateMapTypeCode(BYTES.code)),

    COMPOSITE_LIST(calculateListTypeCode(0x40)),
    COMPOSITE_MAP(calculateMapTypeCode(0x40)),

    UUID(calculateSemanticTypeCode(BYTES.code, 0)),
    UUID_LIST(calculateListTypeCode(UUID.code)),
    UUID_MAP(calculateMapTypeCode(UUID.code));


    public static final short LIST_TYPE = 0x08;
    public static final short MAP_TYPE = 0x10;
    public static final short SEMANTIC_TYPE = 0x80;

    static int calculateListTypeCode(int baseTypeCode) {
        return baseTypeCode | LIST_TYPE;
    }

    static int calculateMapTypeCode(int baseTypeCode) {
        return baseTypeCode | MAP_TYPE;
    }


    private static final short SEMANTIC_SLOT_SHIFT = 0x8;
    static int calculateSemanticTypeCode(int baseTypeCode, int slot) {
        return baseTypeCode | SEMANTIC_TYPE | (slot << SEMANTIC_SLOT_SHIFT);
    }


    private final int code;
    private final String debugName;


    IdentifierType(int code) {
        this.code = code;
        this.debugName = name().toLowerCase().replace('_', '-'); // kebab-case
    }

    /**
     * The type code for this type.
     *
     * @return the type code.
     */
    public int code() {
        return code;
    }


    @Override
    public String toString() {
        return debugName;
    }
}
