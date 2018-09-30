package io.identifiers.types;

import java.util.List;

interface ListTypeTemplate<T> extends TypeTemplate<List<T>> {
    List<T> initialValues(List<T> values);
}
