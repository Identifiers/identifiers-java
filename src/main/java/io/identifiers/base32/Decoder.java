/*
  This base32 algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base32;

import java.util.Arrays;

import static io.identifiers.base32.Constants.BYTE_MASK;
import static io.identifiers.base32.Constants.BYTE_SHIFT_START;
import static io.identifiers.base32.Constants.BYTE_SIZE;
import static io.identifiers.base32.Constants.CHECK_EXTRAS;
import static io.identifiers.base32.Constants.CHECK_PRIME;
import static io.identifiers.base32.Constants.PREFIX;
import static io.identifiers.base32.Constants.SYMBOLS;
import static io.identifiers.base32.Constants.WORD_SHIFT_START;
import static io.identifiers.base32.Constants.WORD_SIZE;

public class Decoder {

    private static final long[] CODES = new long[0x100];
    private static final long[] CHECK_CODES = new long[CODES.length];

    private static final char[][] DECODE_ALIASES = {
            "oO".toCharArray(),
            "iIlL".toCharArray()
    };

    private static final byte[] EMPTY_BYTES = new byte[0];

    static {
        Arrays.fill(CODES, -1);
        for (int i = 0; i < SYMBOLS.length(); i++) {
            char charCode = SYMBOLS.charAt(i);
            CODES[charCode] = i;
            char upperCode = Character.toUpperCase(charCode);
            if (charCode != upperCode) {
                CODES[upperCode] = i;
            }
        }

        // aliases
        for (int row = 0; row < DECODE_ALIASES.length; row++) {
            long code = CODES[Integer.toString(row).charAt(0)];
            for (char aliasCode : DECODE_ALIASES[row]) {
                CODES[aliasCode] = code;
            }
        }

        // checksum
        System.arraycopy(CODES, 0, CHECK_CODES, 0, CODES.length);
        for (int i = 0; i < CHECK_EXTRAS.length(); i++) {
            CHECK_CODES[CHECK_EXTRAS.charAt(i)] = 0x20 + i;
        }

        // alias the 'u' to uppercase
        CHECK_CODES['U'] = CHECK_CODES['u'];
    }


    public static byte[] decode(String encoded) {
        if (encoded.equals(PREFIX)) {
            return EMPTY_BYTES;
        }

        int length = encoded.length() - 2;
        int bytesCount = length * WORD_SIZE / BYTE_SIZE;
        int fullWordsEnd = bytesCount / WORD_SIZE * WORD_SIZE;
        byte[] result = new byte[bytesCount];

        int charPos = 1;
        int bytePos = 0;
        long checksum = 0;

        while (bytePos < fullWordsEnd) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; shift > -1; shift -= WORD_SIZE) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; shift > -1; shift -= BYTE_SIZE) {
                byte aByte = unpackByte(unpacked, shift);
                result[bytePos++] = aByte;
                checksum = updateChecksum(checksum, aByte);
            }
        }

        // remainder
        if (bytePos < bytesCount) {
            long unpacked = 0;

            for (int shift = WORD_SHIFT_START; charPos <= length; shift -= WORD_SIZE) {
                unpacked = unpackChar(encoded, charPos++, unpacked, shift);
            }

            for (int shift = BYTE_SHIFT_START; bytePos < bytesCount; shift -= BYTE_SIZE) {
                byte aByte = unpackByte(unpacked, shift);
                result[bytePos++] = aByte;
                checksum = updateChecksum(checksum, aByte);
            }
        }

        checksum %= CHECK_PRIME;
        char checkDigit = encoded.charAt(charPos);
        if (CHECK_CODES[checkDigit] != checksum) {
            throw new IllegalArgumentException(String.format(
                    "Incorrect string -- check digits do not match. Expected: %s, but calculated %s",
                    CHECK_CODES[checkDigit], checksum));
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

    private static long updateChecksum(long checksum, byte aByte) {
        return checksum + (aByte & BYTE_MASK);
    }
}
