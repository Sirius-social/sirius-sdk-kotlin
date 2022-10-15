package com.sirius.library.utils.json_canonical

internal class NumberFastDtoaBuilder {
    // allocate buffer for generated digits + extra notation + padding zeroes
    val chars = CharArray(NumberFastDtoa.kFastDtoaMaximalLength + 8)
    var end = 0
    var point = 0
    var formatted = false
    fun append(c: Char) {
        chars[end++] = c
    }

    fun decreaseLast() {
        chars[end - 1]--
    }

    fun reset() {
        end = 0
        formatted = false
    }

    override fun toString(): String {
        return "[chars:" + chars.concatToString(0, 0 + end) + ", point:" + point + "]"
    }

    fun format(): String {
        if (!formatted) {
            // check for minus sign
            val firstDigit = if (chars[0] == '-') 1 else 0
            val decPoint = point - firstDigit
            if (decPoint < -5 || decPoint > 21) {
                toExponentialFormat(firstDigit, decPoint)
            } else {
                toFixedFormat(firstDigit, decPoint)
            }
            formatted = true
        }
        return chars.concatToString(0, 0 + end)
    }

    private fun toFixedFormat(firstDigit: Int, decPoint: Int) {
        if (point < end) {
            // insert decimal point
            if (decPoint > 0) {
                // >= 1, split decimals and insert point
                chars.copyInto(chars,point,point + 1,end - point)
               // java.lang.System.arraycopy(chars, point, chars, point + 1, end - point)
                chars[point] = '.'
                end++
            } else {
                // < 1,
                val target = firstDigit + 2 - decPoint
                chars.copyInto(chars,firstDigit,target,end - firstDigit)
              //  java.lang.System.arraycopy(chars, firstDigit, chars, target, end - firstDigit)
                chars[firstDigit] = '0'
                chars[firstDigit + 1] = '.'
                if (decPoint < 0) {
                    chars.fill('0',firstDigit + 2,target,)
                  //  java.util.Arrays.fill(chars, firstDigit + 2, target, '0')
                }
                end += 2 - decPoint
            }
        } else if (point > end) {
            // large integer, add trailing zeroes
            chars.fill( '0',end,point)
          //  java.util.Arrays.fill(chars, end, point, '0')
            end += point - end
        }
    }

    private fun toExponentialFormat(firstDigit: Int, decPoint: Int) {
        if (end - firstDigit > 1) {
            // insert decimal point if more than one digit was produced
            val dot = firstDigit + 1
            chars.copyInto(chars,dot,dot + 1, end - dot)
            //
          //  src – the source array. srcPos – starting position in the source array. dest – the destination array. destPos – starting position in the destination data. length – the number of array elements to be copied.
          //  java.lang.System.arraycopy(chars, dot, chars, dot + 1, end - dot)
            chars[dot] = '.'
            end++
        }
        chars[end++] = 'e'
        var sign = '+'
        var exp = decPoint - 1
        if (exp < 0) {
            sign = '-'
            exp = -exp
        }
        chars[end++] = sign
        var charPos = if (exp > 99) end + 2 else if (exp > 9) end + 1 else end
        end = charPos + 1

        // code below is needed because Integer.getChars() is not public
        while (true) {
            val r = exp % 10
            chars[charPos--] = digits[r]
            exp = exp / 10
            if (exp == 0) break
        }
    }

    companion object {
        val digits = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        )
    }
}
