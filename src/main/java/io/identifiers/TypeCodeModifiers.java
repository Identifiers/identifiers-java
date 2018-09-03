package io.identifiers;

public final class TypeCodeModifiers {
    public static final short LIST_TYPE_CODE = 0x08;
    public static final short MAP_TYPE_CODE = 0x10;
    public static final short LIST_OF = 0x20;
    public static final short LIST_OF_LISTS =LIST_OF | LIST_TYPE_CODE;
    public static final short LIST_OF_MAPS =LIST_OF | MAP_TYPE_CODE;
}
