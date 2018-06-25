package io.identifiers;

public enum IdentifierType {
    STRING(0x0),
    BOOLEAN(0x1),
    INTEGER(0x2),
    FLOAT(0x3),
    LONG(0x4),
    BYTES(0x5);


    private final int code;

    IdentifierType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
