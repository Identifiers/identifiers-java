package io.identifiers;

import java.util.List;
import java.util.Map;

public interface IdentifierFactory {
    <T, INPUT> Identifier<T> create(INPUT data);

    <T, INPUT> List<Identifier<T>> createList(Iterable<INPUT> data);

    <T, INPUT> Map<String, T> createMap(Iterable<Map.Entry<String, INPUT>> dataEntries);

    <T, INPUT> Map<String, T> createMap(Map<String, INPUT> dataMap);
}
