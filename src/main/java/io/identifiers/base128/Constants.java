/*
  This base128 algorithm is based on Mikael Grev's MiGBase64 algorithm: http://migbase64.sourceforge.net
  which is licensed under the BSD Open Source license.
 */
package io.identifiers.base128;

public final class Constants {
    static final String  SYMBOLS ="/0123456789?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüý";
    static final String TERMINATOR ="þ";

    static final int WORD_SIZE = 0x7;
    static final int BYTE_SIZE = 0x8;

    static final long BYTE_MASK = 0xff;

    private static int CHARS_PER_WORD = 0x8;
    static int BYTE_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - BYTE_SIZE;
    static int WORD_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - WORD_SIZE;
}
