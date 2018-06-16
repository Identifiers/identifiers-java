/*
  This codec's algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base32;

public class Constants {

    /**
     * Douglas Crockford's base-32 symbols, lowercased.
     * See http://www.crockford.com/wrmg/base32.html
     */
    static final String SYMBOLS = "0123456789abcdefghjkmnpqrstvwxyz";
    static final String CHECK_EXTRAS = "*~$=u";

    static final String PREFIX = "_";
    static final int CHECK_PRIME = SYMBOLS.length() + CHECK_EXTRAS.length();
    static final int WORD_SIZE = 0x5;
    static final long BYTE_MASK = 0xff;
    static final int BYTE_SHIFT = 0x8;

    private static final int CHARS_PER_WORD = 0x8;
    static final int BYTE_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - BYTE_SHIFT;
    static final int WORD_SHIFT = 0x5;
    static final int WORD_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - WORD_SHIFT;
}
