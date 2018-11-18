package io.identifiers.types;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import io.identifiers.Assert;
import io.identifiers.Identifier;
import io.identifiers.base128.Base128Decoder;
import io.identifiers.base32.Base32Decoder;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

public final class IdentifierDecoderSupport {

    private static final int BUFFER_SIZE = 2048;

    private static final MessagePack.UnpackerConfig UNPACKER = new MessagePack.UnpackerConfig()
        .withBufferSize(BUFFER_SIZE)
        .withStringDecoderBufferSize(BUFFER_SIZE)
        .withActionOnMalformedString(CodingErrorAction.REPORT)
        .withActionOnUnmappableString(CodingErrorAction.REPORT);

    private IdentifierDecoderSupport() {
        // static class
    }

    /**
     * Parse an encoded identifier string into an Identifier instance.
     *
     * @param encodedString the encoded string
     * @param <T> The type of the identifier
     * @return an Identifier instance
     * @throws IllegalArgumentException if the string is not an encoded identifier
     */
    public static <T> Identifier<T> decodeIdentifier(String encodedString) {
        if (Base128Decoder.maybe(encodedString)) {
            return decodeDataString(encodedString);
        }
        return decodeHumanString(encodedString);
    }

    private static <T> Identifier<T> decodeDataString(String encodedString) {
        byte[] bytes = Base128Decoder.decode(encodedString);
        return bytesToIdentifier(bytes);
    }

    private static <T> Identifier<T> decodeHumanString(String encodedString) {
        byte[] bytes = Base32Decoder.decode(encodedString);
        return bytesToIdentifier(bytes);
    }

    private static <T> Identifier<T> bytesToIdentifier(byte[] bytes) {
        try (MessageUnpacker unpacker = UNPACKER.newUnpacker(bytes)) {
            return unpackIdentifier(unpacker);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("cannot decode msgPack %s", Arrays.toString(bytes)), e);
        }
    }

    static <T> Identifier<T> unpackIdentifier(MessageUnpacker unpacker) throws IOException {
        Integer typeCode = unpackTypeCode(unpacker);
        return decodeIdentifier(typeCode, unpacker);
    }

    private static Integer unpackTypeCode(MessageUnpacker unpacker) throws IOException {
        int arraySize = unpacker.unpackArrayHeader();
        Assert.state(arraySize == 2, "expected array size of 2, but found %d", arraySize);
        return unpacker.unpackInt();
    }

    @SuppressWarnings("unchecked")
    private static <T> Identifier<T> decodeIdentifier(Integer typeCode, MessageUnpacker unpacker) throws IOException {
        return IdentifierDecoderProvider
            .findDecoder(typeCode)
            .decode(unpacker);
    }
}

