package io.identifiers.base128;

import static io.identifiers.base128.Constants.BYTE_SHIFT;
import static io.identifiers.base128.Constants.BYTE_SHIFT_START;
import static io.identifiers.base128.Constants.SYMBOLS;
import static io.identifiers.base128.Constants.TERMINATOR;
import static io.identifiers.base128.Constants.WORD_SHIFT;
import static io.identifiers.base128.Constants.WORD_SHIFT_START;
import static io.identifiers.base128.Constants.WORD_SIZE;

import java.util.Arrays;

public final class Decoder {

    private static final long[] CODES = new long[0x100];

    static {
        Arrays.fill(CODES, -1);
        for (int i = 0; i < SYMBOLS.length(); i++) {
            CODES[SYMBOLS.charAt(i)] = i;
        }
    }

    private static final byte[] EMPTY_BYTES = new byte[0];


    public static byte[] decode(String encoded) {
        if (encoded.equals(TERMINATOR)) {
            return EMPTY_BYTES;
        }

        int length = encoded.length() - 1;
        int bytesCount = length * WORD_SIZE / BYTE_SHIFT;
        int fullWordsEnd = bytesCount / WORD_SIZE * WORD_SIZE;
        byte[] result = new byte[bytesCount];

        int charPos = 0;
        int bytePos = 0;


        while (bytePos < fullWordsEnd) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; shift > -1; shift -= WORD_SHIFT) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; shift > -1; shift -=  BYTE_SHIFT) {
                result[bytePos++] = unpackByte(unpacked, shift);
            }
        }

        if (bytePos < bytesCount) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; charPos < length; shift -= WORD_SHIFT) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; bytePos < bytesCount; shift -= BYTE_SHIFT) {
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
