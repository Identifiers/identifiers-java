package io.identifiers;

public class IdentifierType {

    private final Integer code;
    private final String name;

    public IdentifierType(String name, int code) {
        this.code = code;
        this.name = name;
    }

    public String name() {
        return name;
    }

    /**
     * The type code for this type.
     *
     * @return the type code.
     */
    public Integer code() {
        return code;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof IdentifierType) {
            IdentifierType that = (IdentifierType) other;
            return code.equals(that.code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public static final IdentifierType STRING = new IdentifierType("string", 0x00);
    public static final IdentifierType BOOLEAN = new IdentifierType("boolean", 0x01);
    public static final IdentifierType INTEGER = new IdentifierType("integer", 0x02);
    public static final IdentifierType FLOAT = new IdentifierType("float", 0x03);
    public static final IdentifierType LONG = new IdentifierType("long", 0x04);
    public static final IdentifierType BYTES = new IdentifierType("bytes", 0x05);

    public static final IdentifierType STRING_LIST = listType(STRING);
    public static final IdentifierType BOOLEAN_LIST = listType(BOOLEAN);
    public static final IdentifierType INTEGER_LIST = listType(INTEGER);
    public static final IdentifierType FLOAT_LIST = listType(FLOAT);
    public static final IdentifierType LONG_LIST = listType(LONG);
    public static final IdentifierType BYTES_LIST = listType(BYTES);

    public static final IdentifierType STRING_MAP = mapType(STRING);
    public static final IdentifierType BOOLEAN_MAP = mapType(BOOLEAN);
    public static final IdentifierType INTEGER_MAP = mapType(INTEGER);
    public static final IdentifierType FLOAT_MAP = mapType(FLOAT);
    public static final IdentifierType LONG_MAP = mapType(LONG);
    public static final IdentifierType BYTES_MAP = mapType(BYTES);

    public static final IdentifierType COMPOSITE_LIST = new IdentifierType("composite-list",
        Modifiers.calculateListTypeCode(Modifiers.COMPOSITE_TYPE));
    public static final IdentifierType COMPOSITE_MAP = new IdentifierType("composite-map",
        Modifiers.calculateMapTypeCode(Modifiers.COMPOSITE_TYPE));

    public static final IdentifierType UUID = new IdentifierType("uuid", Modifiers.semanticTypeCode(BYTES, 0));
    public static final IdentifierType UUID_LIST = listType(UUID);
    public static final IdentifierType UUID_MAP = mapType(UUID);

    public static final IdentifierType DATETIME = new IdentifierType("datetime", Modifiers.semanticTypeCode(LONG, 1));
    public static final IdentifierType DATETIME_LIST = listType(DATETIME);
    public static final IdentifierType DATETIME_MAP = mapType(DATETIME);

    public static final IdentifierType GEO = new IdentifierType("geo", Modifiers.semanticTypeCode(FLOAT_LIST, 2));
    public static final IdentifierType GEO_LIST = listType(GEO);
    public static final IdentifierType GEO_MAP = mapType(GEO);


    private static IdentifierType listType(final IdentifierType baseType) {
        return new IdentifierType(String.format("%s-list", baseType.name), Modifiers.listTypeCode(baseType));
    }

    private static IdentifierType mapType(final IdentifierType baseType) {
        return new IdentifierType(String.format("%s-map", baseType.name), Modifiers.mapTypeCode(baseType));
    }


    public static final class Modifiers {
        public static final int LIST_TYPE = 0x08;
        public static final int MAP_TYPE = 0x10;
        public static final int SEMANTIC_TYPE = 0x80;

        private static final int COMPOSITE_TYPE = 0x40;
        private static final int SEMANTIC_SLOT_SHIFT = 0x8;

        private Modifiers() {
            // static class
        }

        private static int listTypeCode(IdentifierType baseType) {
            return calculateListTypeCode(baseType.code);
        }

        private static int mapTypeCode(IdentifierType baseType) {
            return calculateMapTypeCode(baseType.code);
        }

        private static int semanticTypeCode(IdentifierType baseType, int slot) {
            return baseType.code | SEMANTIC_TYPE | (slot << SEMANTIC_SLOT_SHIFT);
        }

        private static int calculateListTypeCode(final int typeCode) {
            return typeCode | LIST_TYPE;
        }

        private static int calculateMapTypeCode(final int typeCode) {
            return typeCode | MAP_TYPE;
        }
    }
}
