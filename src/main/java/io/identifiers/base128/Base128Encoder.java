/*
  This base128 algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base128;

import static io.identifiers.base128.Constants.BYTE_MASK;
import static io.identifiers.base128.Constants.BYTE_SIZE;
import static io.identifiers.base128.Constants.BYTE_SHIFT_START;
import static io.identifiers.base128.Constants.SYMBOLS;
import static io.identifiers.base128.Constants.WORD_SHIFT_START;
import static io.identifiers.base128.Constants.WORD_SIZE;

public class Base128Encoder {

    private Base128Encoder() {
        // static class
    }

    private static final byte BITS_MASK = 0x7f;
    private static final char[] CODES = SYMBOLS.toCharArray();


    public static String encode(byte[] unencoded) {
        float wordCount = (float) unencoded.length / WORD_SIZE;
        int charCount = (int) Math.ceil(wordCount * BYTE_SIZE);
        int fullWordsEnd = (int) Math.floor(wordCount) * WORD_SIZE;
        char[] result = new char[charCount];

        int bytePos = 0;
        int charPos = 0;

        while (bytePos < fullWordsEnd) {
            long packed = 0;

            for (int shift = BYTE_SHIFT_START; shift > -1; shift -= BYTE_SIZE) {
                long lByte = toLong(unencoded[bytePos++]);
                packed = packByte(lByte, packed, shift);
            }

            for (int shift = WORD_SHIFT_START; shift > -1; shift -= WORD_SIZE) {
                result[charPos++] = packChar(packed, shift);
            }
        }

        // remainder
        if (bytePos < unencoded.length) {
            long packed = 0;

            for (int shift = BYTE_SHIFT_START; bytePos < unencoded.length; shift -= BYTE_SIZE) {
                long lByte = toLong(unencoded[bytePos++]);
                packed = packByte(lByte, packed, shift);
            }

            int remainder = unencoded.length - fullWordsEnd;
            for (int shift = WORD_SHIFT_START; remainder > -1; shift -= WORD_SIZE) {
                result[charPos++] = packChar(packed, shift);
                remainder--;
            }
        }

        return new String(result);
    }

    private static long toLong(byte aByte) {
        return (long) aByte & BYTE_MASK;
    }

    private static long packByte(long lByte, long packed, int shift) {
        return packed | lByte << shift;
    }

    private static char packChar(long packed, int shift) {
        return CODES[(int) (packed >> shift) & BITS_MASK];
    }
}
