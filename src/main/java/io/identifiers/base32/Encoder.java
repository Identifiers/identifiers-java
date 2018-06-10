/*
  This codec's algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base32;

import java.util.Arrays;

import static io.identifiers.base32.Constants.BYTE_MASK;
import static io.identifiers.base32.Constants.BYTE_SHIFT;
import static io.identifiers.base32.Constants.BYTE_SHIFT_START;
import static io.identifiers.base32.Constants.CHECK_EXTRAS;
import static io.identifiers.base32.Constants.CHECK_PRIME;
import static io.identifiers.base32.Constants.PREFIX;
import static io.identifiers.base32.Constants.SYMBOLS;
import static io.identifiers.base32.Constants.WORD_SHIFT;
import static io.identifiers.base32.Constants.WORD_SHIFT_START;
import static io.identifiers.base32.Constants.WORD_SIZE;

public class Encoder {
    private static final byte BITS_MASK = 0x1f;
    private static final char PREFIX_CODE = PREFIX.charAt(0);
    private static final char[] CODES = SYMBOLS.toCharArray();
    private static final char[] CHECK_CODES = Arrays.copyOf(CODES, CODES.length + CHECK_EXTRAS.length());

    static {
        System.arraycopy(CHECK_EXTRAS.toCharArray(), 0, CHECK_CODES, CODES.length, CHECK_EXTRAS.length());
    }

    public static String encode(byte[] unencoded) {

        if (unencoded.length == 0) {
            return PREFIX;
        }

        float wordCount = ((float) unencoded.length) / WORD_SIZE;
        int charCount = (int) Math.ceil(wordCount * BYTE_SHIFT) + 2;
        int fullWordsEnd = (int) Math.floor(wordCount) * WORD_SIZE;
        char[] result = new char[charCount];

        result[0] = PREFIX_CODE;

        int charPos = 1;
        int bytePos = 0;
        long checksum = 0;

        while (bytePos < fullWordsEnd) {
            long packed = 0;

            for (int shift = BYTE_SHIFT_START; shift > -1; shift -= BYTE_SHIFT) {
                long lByte = toLong(unencoded[bytePos++]);
                packed = packByte(lByte, packed, shift);
                checksum += lByte;
            }

            for (int shift = WORD_SHIFT_START; shift > -1; shift -= WORD_SHIFT) {
                result[charPos++] = packChar(packed, shift);
            }
        }

        // remainder
        if (bytePos < unencoded.length) {
            long packed = 0;
            for (int shift = BYTE_SHIFT_START; bytePos < unencoded.length; shift -= BYTE_SHIFT) {
                long lByte = toLong(unencoded[bytePos++]);
                packed = packByte(lByte, packed, shift);
                checksum += lByte;
            }

            int remainder = unencoded.length - fullWordsEnd;
            int shift = WORD_SHIFT_START;

            result[charPos++] = packChar(packed,shift);
            result[charPos++] = packChar(packed,shift -= WORD_SHIFT);

            if (remainder > 1) {
                result[charPos++] = packChar(packed, shift -= WORD_SHIFT);
                result[charPos++] = packChar(packed, shift -= WORD_SHIFT);
            }

            if (remainder > 2) {
                result[charPos++] = packChar(packed, shift -= WORD_SHIFT);
            }

            if (remainder > 3) {
                result[charPos++] = packChar(packed, shift - WORD_SHIFT);
                result[charPos++] = packChar(packed, WORD_SIZE);
            }

        }

        result[charPos] = CHECK_CODES[(int) checksum % CHECK_PRIME];

        return new String(result);
    }

    private static long toLong(byte aByte) {
        return (long) aByte & BYTE_MASK;
    }

    private static long packByte(long lByte, long packed, int shift) {
        return packed | lByte << shift;
    }

    private static char packChar(long packed, int shift) {
        return CODES[(int) (packed >> shift) & BITS_MASK]; // right shift unsigned?
    }
}
