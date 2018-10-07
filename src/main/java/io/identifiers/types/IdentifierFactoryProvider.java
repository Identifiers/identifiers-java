package io.identifiers.types;

import java.util.EnumMap;
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

    private static final Map<IdentifierType, IdentifierFactory<?>> factoryMap = new EnumMap<>(IdentifierType.class);

    static {
        addTypedFactory(IdentifierType.STRING,
            TypeTemplates.forString,
            ListTypeTemplates.forStringList,
            MapTypeTemplates.forStringMap);

        addTypedFactory(IdentifierType.BOOLEAN,
            TypeTemplates.forBoolean,
            ListTypeTemplates.forBooleanList,
            MapTypeTemplates.forBooleanMap);

        addTypedFactory(IdentifierType.INTEGER,
            TypeTemplates.forInteger,
            ListTypeTemplates.forIntegerList,
            MapTypeTemplates.forIntegerMap);

        addTypedFactory(IdentifierType.FLOAT,
            TypeTemplates.forFloat,
            ListTypeTemplates.forFloatList,
            MapTypeTemplates.forFloatMap);

        addTypedFactory(IdentifierType.LONG,
            TypeTemplates.forLong,
            ListTypeTemplates.forLongList,
            MapTypeTemplates.forLongMap);

        addTypedFactory(IdentifierType.BYTES,
            TypeTemplates.forBytes,
            ListTypeTemplates.forBytesList,
            MapTypeTemplates.forBytesMap);

        addTypedFactory(IdentifierType.UUID,
            TypeTemplates.forUuid,
            ListTypeTemplates.forUuidList,
            MapTypeTemplates.forUuidMap);

        addTypedFactory(IdentifierType.DATETIME,
            TypeTemplates.forDatetime,
            ListTypeTemplates.forDatetimeList,
            MapTypeTemplates.forDatetimeMap);
    }

    @SuppressWarnings("unchecked")
    public static <T> IdentifierFactory<T> typedFactory(IdentifierType type) {
        IdentifierFactory<T> factory = (IdentifierFactory<T>) factoryMap.get(type);
        Assert.argumentExists(factory, "No factory for %s", type);
        return factory;
    }

    @SuppressWarnings("unchecked")
    public static CompositeIdentifierFactory compositeFactory() {
        MapIdentifierFactory<Identifier<?>> mapFactory = new ImmutableMapIdentifierFactory(MapTypeTemplates.forCompositeMap);
        ListIdentifierFactory<Identifier<?>> listFactory = new ImmutableListIdentifierFactory(ListTypeTemplates.forCompositeList);

        return new CompositeIdentifierFactoryImpl(listFactory, mapFactory);
    }


    private static <T> void addTypedFactory(
            IdentifierType type,
            TypeTemplate<T> singleTemplate,
            ListTypeTemplate<T> listTemplate,
            MapTypeTemplate<T> mapTemplate) {

        SingleIdentifierFactory<T> singleFactory = new ImmutableIdentifierFactory<>(singleTemplate);
        ListIdentifierFactory<T> listFactory = new ImmutableListIdentifierFactory<>(listTemplate);
        MapIdentifierFactory<T> mapFactory = new ImmutableMapIdentifierFactory<>(mapTemplate);
        IdentifierFactory<T> factory = new TypedIdentifierFactory<>(singleFactory, listFactory, mapFactory);

        factoryMap.put(type, factory);
    }
}
