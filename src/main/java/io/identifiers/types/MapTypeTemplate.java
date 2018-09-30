package io.identifiers.types;

import java.util.SortedMap;

interface MapTypeTemplate<T> extends TypeTemplate<SortedMap<String, T>> {
    SortedMap<String, T> initialValueMap(SortedMap<String, T> valueMap);
}
