package io.identifiers.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ListSupport {

    private ListSupport() {
        // static class
    }

    static <T> List<T> copyValuesIntoList(Iterator<T> values) {
        List<T> target = new ArrayList<>();
        return copyInto(values, target);
    }

    static <T> List<T> copyValuesIntoList(Iterator<T> values, int size) {
        List<T> target = new ArrayList<>(size);
        return copyInto(values, target);
    }

    private static <T> List<T> copyInto(Iterator<T> values, List<T> target) {
        while (values.hasNext()) {
            target.add(values.next());
        }
        return target;
    }
}
