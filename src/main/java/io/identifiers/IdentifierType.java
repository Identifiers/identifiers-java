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

    public static final IdentifierType STRING_LIST_LIST = listType(STRING_LIST);
    public static final IdentifierType BOOLEAN_LIST_LIST = listType(BOOLEAN_LIST);
    public static final IdentifierType INTEGER_LIST_LIST = listType(INTEGER_LIST);
    public static final IdentifierType FLOAT_LIST_LIST = listType(FLOAT_LIST);
    public static final IdentifierType LONG_LIST_LIST = listType(LONG_LIST);
    public static final IdentifierType BYTES_LIST_LIST = listType(BYTES_LIST);

    public static final IdentifierType STRING_LIST_MAP = mapType(STRING_LIST);
    public static final IdentifierType BOOLEAN_LIST_MAP = mapType(BOOLEAN_LIST);
    public static final IdentifierType INTEGER_LIST_MAP = mapType(INTEGER_LIST);
    public static final IdentifierType FLOAT_LIST_MAP = mapType(FLOAT_LIST);
    public static final IdentifierType LONG_LIST_MAP = mapType(LONG_LIST);
    public static final IdentifierType BYTES_LIST_MAP = mapType(BYTES_LIST);

    public static final IdentifierType STRING_MAP_LIST = listType(STRING_MAP);
    public static final IdentifierType BOOLEAN_MAP_LIST = listType(BOOLEAN_MAP);
    public static final IdentifierType INTEGER_MAP_LIST = listType(INTEGER_MAP);
    public static final IdentifierType FLOAT_MAP_LIST = listType(FLOAT_MAP);
    public static final IdentifierType LONG_MAP_LIST = listType(LONG_MAP);
    public static final IdentifierType BYTES_MAP_LIST = listType(BYTES_MAP);

    public static final IdentifierType STRING_MAP_MAP = mapType(STRING_MAP);
    public static final IdentifierType BOOLEAN_MAP_MAP = mapType(BOOLEAN_MAP);
    public static final IdentifierType INTEGER_MAP_MAP = mapType(INTEGER_MAP);
    public static final IdentifierType FLOAT_MAP_MAP = mapType(FLOAT_MAP);
    public static final IdentifierType LONG_MAP_MAP = mapType(LONG_MAP);
    public static final IdentifierType BYTES_MAP_MAP = mapType(BYTES_MAP);

    public static final IdentifierType COMPOSITE_LIST = new IdentifierType("composite-list", 0x38);
    public static final IdentifierType COMPOSITE_MAP = new IdentifierType("composite-map", 0x58);

    public static final IdentifierType UUID = new IdentifierType("uuid", Modifiers.semanticTypeCode(BYTES, 0));
    public static final IdentifierType UUID_LIST = listType(UUID);
    public static final IdentifierType UUID_MAP = mapType(UUID);

    public static final IdentifierType DATETIME = new IdentifierType("datetime", Modifiers.semanticTypeCode(LONG, 1));
    public static final IdentifierType DATETIME_LIST = listType(DATETIME);
    public static final IdentifierType DATETIME_MAP = mapType(DATETIME);

    public static final IdentifierType GEO = new IdentifierType("geo", Modifiers.semanticTypeCode(FLOAT_LIST, 2));
    public static final IdentifierType GEO_LIST = listType(GEO);
    public static final IdentifierType GEO_MAP = mapType(GEO);


    private static IdentifierType listType(IdentifierType baseType) {
        return new IdentifierType(String.format("%s-list", baseType.name), Modifiers.listTypeCode(baseType));
    }

    private static IdentifierType mapType(IdentifierType baseType) {
        return new IdentifierType(String.format("%s-map", baseType.name), Modifiers.mapTypeCode(baseType));
    }


    public static final class Modifiers {
        public static final int SEMANTIC_TYPE = 0x80;

        private static final int LIST_TYPE = 0x08;
        private static final int MAP_TYPE = 0x10;
        private static final int LIST_OF = 0x20;
        private static final int MAP_OF = 0x40;
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

        private static int calculateListTypeCode(int typeCode) {
            Assert.state((typeCode & LIST_OF) == 0,
                "Cannot create a List of List of Something. typeCode: %d", typeCode);
            Assert.state((typeCode & MAP_OF) == 0,
                "Cannot create a List of Map of of Something. typeCode: %d", typeCode);
            return typeCode | (isStructural(typeCode) ? LIST_OF : LIST_TYPE);
        }

        private static int calculateMapTypeCode(int typeCode) {
            Assert.state((typeCode & LIST_OF) == 0,
                    "Cannot create a Map of List of Something. typeCode: %d", typeCode);
            Assert.state((typeCode & MAP_OF) == 0,
                    "Cannot create a Map of Map of of Something. typeCode: %d", typeCode);
            return typeCode | (isStructural(typeCode) ? MAP_OF : MAP_TYPE);
        }

        private static boolean isStructural(int typeCode) {
            return (typeCode & LIST_TYPE) > 0 || (typeCode & MAP_TYPE) > 0;
        }
    }
}
