package io.identifiers.types;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

final class MapSupport {

    private MapSupport() {
        //static class
    }

    static <T> SortedMap<String, T> copyToSortedMap(Map<String, T> map) {
        return new TreeMap<>(map);
    }

    static <T> SortedMap<String, T> copyToSortedMap(Iterator<Map.Entry<String, T>> entries) {
        SortedMap<String, T> copied = new TreeMap<>();
        while (entries.hasNext()) {
            Map.Entry<String, T> entry = entries.next();
            copied.put(entry.getKey(), entry.getValue());
        }
        return copied;
    }

    static <T> T mergeAcceptFirst(T t1, T t2) {
        return t1;
    }
}
