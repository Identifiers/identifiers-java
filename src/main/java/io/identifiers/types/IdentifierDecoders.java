package io.identifiers.types;

import java.io.IOException;
import java.util.Arrays;

import io.identifiers.Assert;
import io.identifiers.Identifier;
import io.identifiers.base128.Base128Decoder;
import io.identifiers.base32.Base32Decoder;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

public final class IdentifierDecoders {

    /**
     * Parse an encoded identifier string into an Identifier instance.
     *
     * @param encodedString the encoded string
     * @return an Identifier instance
     * @throws IllegalArgumentException if the string is not an encoded identifier
     */
    @SuppressWarnings("unchecked")
    public static <T> Identifier<T> decodeIdentifier(String encodedString) {
        if (Base128Decoder.maybe(encodedString)) {
            return decodeDataString(encodedString);
        }
        if (Base32Decoder.maybe(encodedString)) {
            return decodeHumanString(encodedString);
        }
        throw new IllegalArgumentException(String.format("Cannot decode '%s'", encodedString));
    }

    private static Identifier decodeDataString(String encodedString) {
        byte[] bytes = Base128Decoder.decode(encodedString);
        return bytesToIdentifier(bytes);
    }

    private static Identifier decodeHumanString(String encodedString) {
        byte[] bytes = Base32Decoder.decode(encodedString);
        return bytesToIdentifier(bytes);
    }

    private static Identifier bytesToIdentifier(byte[] bytes) {
        try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes)) {
            int typeCode = unpackTypeCode(unpacker);
            return unpackIdentifier(typeCode, unpacker);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot decode msgPack %s", Arrays.toString(bytes)), e);
        }
    }

    private static int unpackTypeCode(final MessageUnpacker unpacker) throws IOException {
        int arraySize = unpacker.unpackArrayHeader();
        Assert.state(arraySize == 2, "expected array size of 2, but found %d", arraySize);
        return unpacker.unpackInt();
    }

    private static Identifier unpackIdentifier(final int typeCode, final MessageUnpacker unpacker) throws IOException {
        return IdentifierDecoderProvider.findDecoder(typeCode).decode(unpacker);
    }
}

