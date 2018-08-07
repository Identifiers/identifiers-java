package io.identifiers;

public enum IdentifierType {

    STRING(0x0),
    BOOLEAN(0x1),
    INTEGER(0x2),
    FLOAT(0x3),
    LONG(0x4),
    BYTES(0x5),

    STRING_LIST(STRING.code | TypeCodeModifiers.LIST_TYPE_CODE),
    BOOLEAN_LIST(BOOLEAN.code | TypeCodeModifiers.LIST_TYPE_CODE),
    INTEGER_LIST(INTEGER.code | TypeCodeModifiers.LIST_TYPE_CODE),
    FLOAT_LIST(FLOAT.code | TypeCodeModifiers.LIST_TYPE_CODE),
    LONG_LIST(LONG.code | TypeCodeModifiers.LIST_TYPE_CODE),
    BYTES_LIST(BYTES.code | TypeCodeModifiers.LIST_TYPE_CODE),

    STRING_MAP(STRING.code | TypeCodeModifiers.MAP_TYPE_CODE),
    BOOLEAN_MAP(BOOLEAN.code | TypeCodeModifiers.MAP_TYPE_CODE),
    INTEGER_MAP(INTEGER.code | TypeCodeModifiers.MAP_TYPE_CODE),
    FLOAT_MAP(FLOAT.code | TypeCodeModifiers.MAP_TYPE_CODE),
    LONG_MAP(LONG.code | TypeCodeModifiers.MAP_TYPE_CODE),
    BYTES_MAP(BYTES.code | TypeCodeModifiers.MAP_TYPE_CODE);


    private final int code;


    IdentifierType(int code) {
        this.code = code;
    }

    /**
     * The type code for this type.
     *
     * @return the type code.
     */
    public int code() {
        return code;
    }
}
