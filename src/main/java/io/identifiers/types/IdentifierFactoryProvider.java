package io.identifiers.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.identifiers.Assert;
import io.identifiers.CompositeIdentifierFactory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;
import io.identifiers.IdentifierType;
import io.identifiers.ListIdentifierFactory;
import io.identifiers.MapIdentifierFactory;
import io.identifiers.SingleIdentifierFactory;

public final class IdentifierFactoryProvider {

    private IdentifierFactoryProvider() {
        // static class
    }

    private static final Map<IdentifierType, IdentifierFactory<?>> factoryMap = new HashMap<>();

    static {
        addTypedFactory(IdentifierType.STRING,
            TypeTemplates.forString,
            TypeTemplates.forStringList,
            TypeTemplates.forStringMap);

        addTypedFactory(IdentifierType.BOOLEAN,
            TypeTemplates.forBoolean,
            TypeTemplates.forBooleanList,
            TypeTemplates.forBooleanMap);

        addTypedFactory(IdentifierType.INTEGER,
            TypeTemplates.forInteger,
            TypeTemplates.forIntegerList,
            TypeTemplates.forIntegerMap);

        addTypedFactory(IdentifierType.FLOAT,
            TypeTemplates.forFloat,
            TypeTemplates.forFloatList,
            TypeTemplates.forFloatMap);

        addTypedFactory(IdentifierType.LONG,
            TypeTemplates.forLong,
            TypeTemplates.forLongList,
            TypeTemplates.forLongMap);

        addTypedFactory(IdentifierType.BYTES,
            TypeTemplates.forBytes,
            TypeTemplates.forBytesList,
            TypeTemplates.forBytesMap);

        addTypedFactory(IdentifierType.UUID,
            TypeTemplates.forUuid,
            TypeTemplates.forUuidList,
            TypeTemplates.forUuidMap);

        addTypedFactory(IdentifierType.DATETIME,
            TypeTemplates.forDatetime,
            TypeTemplates.forDatetimeList,
            TypeTemplates.forDatetimeMap);

        addTypedFactory(IdentifierType.GEO,
            TypeTemplates.forGeo,
            TypeTemplates.forGeoList,
            TypeTemplates.forGeoMap);
    }

    @SuppressWarnings("unchecked")
    public static <T> IdentifierFactory<T> typedFactory(IdentifierType type) {
        IdentifierFactory<T> factory = (IdentifierFactory<T>) factoryMap.get(type);
        Assert.argumentExists(factory, "No factory for %s", type);
        return factory;
    }

    @SuppressWarnings("unchecked")
    public static CompositeIdentifierFactory compositeFactory() {
        MapIdentifierFactory<Identifier<?>> mapFactory = new ImmutableMapIdentifierFactory(TypeTemplates.forCompositeMap);
        ListIdentifierFactory<Identifier<?>> listFactory = new ImmutableListIdentifierFactory(TypeTemplates.forCompositeList);

        return new CompositeIdentifierFactoryImpl(listFactory, mapFactory);
    }


    private static <T> void addTypedFactory(
            IdentifierType type,
            TypeTemplate<T> singleTemplate,
            TypeTemplate<List<T>> listTemplate,
            TypeTemplate<Map<String, T>> mapTemplate) {

        SingleIdentifierFactory<T> singleFactory = new ImmutableIdentifierFactory<>(singleTemplate);
        ListIdentifierFactory<T> listFactory = new ImmutableListIdentifierFactory<>(listTemplate);
        MapIdentifierFactory<T> mapFactory = new ImmutableMapIdentifierFactory<>(mapTemplate);
        IdentifierFactory<T> factory = new TypedIdentifierFactory<>(singleFactory, listFactory, mapFactory);

        factoryMap.put(type, factory);
    }
}
