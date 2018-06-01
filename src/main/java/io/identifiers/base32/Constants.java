/*
 * The MIT License (MIT)
 *
 * Copyright © 2018 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
    static final int CHARS_PER_WORD = 0x8;
    static final int BYTE_MASK = 0xff;
    static final int BYTE_SHIFT = 0x8;
    static final int BYTE_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - BYTE_SHIFT;
    static final int WORD_SHIFT = 0x5;
    static final int WORD_SHIFT_START = WORD_SIZE * CHARS_PER_WORD - WORD_SHIFT;
}
