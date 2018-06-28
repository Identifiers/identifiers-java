package io.identifiers.types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.identifiers.Assert;
import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import org.msgpack.core.MessageUnpacker;

final class IdentifierDecoderProvider {

    static IdentifierDecoder findDecoder(final int typeCode) {
        IdentifierDecoder decoder = decoderMap.get(typeCode);
        Assert.argumentExists(decoder, "cannot unpack identifier type %s", typeCode);
        return decoder;
    }


    @FunctionalInterface
    interface ThrowingFunction<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    private static final Map<Integer, IdentifierDecoder> decoderMap = new HashMap<>();

    static {
        decoderMap.put(
            IdentifierType.STRING.code(), composeDecoder(
                MessageUnpacker::unpackString,
                Factory.forString::create));

        decoderMap.put(
            IdentifierType.BOOLEAN.code(), composeDecoder(
                MessageUnpacker::unpackBoolean,
                Factory.forBoolean::create));

        decoderMap.put(
            IdentifierType.INTEGER.code(), composeDecoder(
                MessageUnpacker::unpackInt,
                Factory.forInteger::create));

        decoderMap.put(
            IdentifierType.FLOAT.code(), composeDecoder(
                MessageUnpacker::unpackFloat,
                Factory.forFloat::create));

        decoderMap.put(
            IdentifierType.LONG.code(), composeDecoder(
                MessageUnpacker::unpackLong,
                Factory.forLong::create));

        decoderMap.put(
            IdentifierType.BYTES.code(), composeDecoder(
                (unpacker) -> {
                    int size = unpacker.unpackBinaryHeader();
                    byte[] bytes = new byte[size];
                    unpacker.readPayload(bytes);
                    return bytes;
                },
                Factory.forBytes::create));
    }

    private static <T> IdentifierDecoder composeDecoder(
        ThrowingFunction<MessageUnpacker, T, IOException> unpackerƒ,
        Function<T, Identifier<T>> factoryƒ) {

        return (unpacker) -> factoryƒ.apply(unpackerƒ.apply(unpacker));
    }
}
