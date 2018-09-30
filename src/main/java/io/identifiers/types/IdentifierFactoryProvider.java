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
        factoryMap.put(IdentifierType.STRING, assembleTypedFactory(
            TypeTemplates.forString,
            ListTypeTemplates.forStringList,
            MapTypeTemplates.forStringMap));

        factoryMap.put(IdentifierType.BOOLEAN, assembleTypedFactory(
            TypeTemplates.forBoolean,
            ListTypeTemplates.forBooleanList,
            MapTypeTemplates.forBooleanMap));

        factoryMap.put(IdentifierType.INTEGER, assembleTypedFactory(
            TypeTemplates.forInteger,
            ListTypeTemplates.forIntegerList,
            MapTypeTemplates.forIntegerMap));

        factoryMap.put(IdentifierType.FLOAT, assembleTypedFactory(
            TypeTemplates.forFloat,
            ListTypeTemplates.forFloatList,
            MapTypeTemplates.forFloatMap));

        factoryMap.put(IdentifierType.LONG, assembleTypedFactory(
            TypeTemplates.forLong,
            ListTypeTemplates.forLongList,
            MapTypeTemplates.forLongMap));

        factoryMap.put(IdentifierType.BYTES, assembleTypedFactory(
            TypeTemplates.forBytes,
            ListTypeTemplates.forBytesList,
            MapTypeTemplates.forBytesMap));
    }

    @SuppressWarnings("unchecked")
    public static <T> IdentifierFactory<T> typedFactory(IdentifierType type) {
        IdentifierFactory<T> factory = (IdentifierFactory<T>) factoryMap.get(type);
        Assert.argumentExists(factory, "No factory for %s", type);
        return factory;
    }

    private static <T> IdentifierFactory<T> assembleTypedFactory(
            TypeTemplate<T> singleTemplate,
            ListTypeTemplate<T> listTemplate,
            MapTypeTemplate<T> mapTemplate) {

        SingleIdentifierFactory<T> singleFactory = new ImmutableIdentifierFactory<>(singleTemplate);
        ListIdentifierFactory<T> listFactory = new ImmutableListIdentifierFactory<>(listTemplate);
        MapIdentifierFactory<T> mapFactory = new ImmutableMapIdentifierFactory<>(mapTemplate);

        return new TypedIdentifierFactory<>(singleFactory, listFactory, mapFactory);
    }

    @SuppressWarnings("unchecked")
    public static CompositeIdentifierFactory compositeFactory() {
        MapIdentifierFactory<Identifier<?>> mapFactory = new ImmutableMapIdentifierFactory(MapTypeTemplates.forCompositeMap);
        ListIdentifierFactory<Identifier<?>> listFactory = new ImmutableListIdentifierFactory(ListTypeTemplates.forCompositeList);

        return new CompositeIdentifierFactoryImpl(listFactory, mapFactory);
    }
}
