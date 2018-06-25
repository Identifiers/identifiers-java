package io.identifiers;

public interface IdentifierFactory<T> {

    Identifier<T> create(T value);

/*
    TODO These need to be ListIdentifier and MapIdentifier

    List<Identifier<T>> createList(Iterable<T> value);

    Map<String, Identifier<T>> createMap(Iterable<Map.Entry<String, T>> valueEntries);

    Map<String, Identifier<T>> createMap(Map<String, T> valueMap);
*/
}
