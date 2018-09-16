/*
  This base128 algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base128;

import static io.identifiers.base128.Constants.BYTE_SHIFT_START;
import static io.identifiers.base128.Constants.BYTE_SIZE;
import static io.identifiers.base128.Constants.SYMBOLS;
import static io.identifiers.base128.Constants.TERMINATOR;
import static io.identifiers.base128.Constants.WORD_SHIFT_START;
import static io.identifiers.base128.Constants.WORD_SIZE;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class Base128Decoder {

    private Base128Decoder() {
        // static class
    }

    private static final long[] CODES = new long[0x100];

    static {
        Arrays.fill(CODES, -1);
        IntStream.range(0, SYMBOLS.length()).forEach(i ->
            CODES[SYMBOLS.charAt(i)] = i);
    }

    private static final byte[] EMPTY_BYTES = new byte[0];


    public static boolean maybe(String encoded) {
        return encoded.length() != 2 && encoded.endsWith(TERMINATOR);
    }

    public static byte[] decode(String encoded) {
        if (encoded.equals(TERMINATOR)) {
            return EMPTY_BYTES;
        }

        int length = encoded.length() - 1;
        int bytesCount = length * WORD_SIZE / BYTE_SIZE;
        int fullWordsEnd = bytesCount / WORD_SIZE * WORD_SIZE;
        byte[] result = new byte[bytesCount];

        int charPos = 0;
        int bytePos = 0;


        while (bytePos < fullWordsEnd) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; shift > -1; shift -= WORD_SIZE) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; shift > -1; shift -= BYTE_SIZE) {
                result[bytePos++] = unpackByte(unpacked, shift);
            }
        }

        if (bytePos < bytesCount) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; charPos < length; shift -= WORD_SIZE) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; bytePos < bytesCount; shift -= BYTE_SIZE) {
                result[bytePos++] = unpackByte(unpacked, shift);
            }
        }

        return result;
    }


    private static long unpackChar(String encoded, int charPos, long unpacked, int shift) {
        char charCode = encoded.charAt(charPos);
        long value = charCode < CODES.length ? CODES[charCode] : -1;
        if (value < 0) {
            throw new IllegalArgumentException(String.format("invalid char code: %s at position %s",
                charCode, charPos));
        }

        return unpacked | value << shift;
    }


    private static byte unpackByte(long unpacked, int shift) {
        return (byte) (unpacked >> shift);
    }
}
