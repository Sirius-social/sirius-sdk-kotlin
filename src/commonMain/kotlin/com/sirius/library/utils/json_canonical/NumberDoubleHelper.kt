package com.sirius.library.utils.json_canonical

internal object NumberDoubleHelper {
    val kSignMask = Long.MIN_VALUE
    const val kExponentMask = 0x7FF0000000000000L
    const val kSignificandMask = 0x000FFFFFFFFFFFFFL
    const val kHiddenBit = 0x0010000000000000L
    fun asDiyFp(d64: Long): NumberDiyFp {
        if(!isSpecial(d64)){
            return NumberDiyFp(significand(d64), exponent(d64))
        }
        throw AssertionError()
    }

    // this->Significand() must not be 0.
    fun asNormalizedDiyFp(d64: Long): NumberDiyFp {
        var f = significand(d64)
        var e = exponent(d64)
        if(f != 0L){

        }else{
            throw AssertionError()
        }

        // The current double could be a denormal.
        while (f and kHiddenBit == 0L) {
            f = f shl 1
            e--
        }
        // Do the final shifts in one go. Don't forget the hidden bit (the '-1').
        f = f shl NumberDiyFp.kSignificandSize - kSignificandSize - 1
        e -= NumberDiyFp.kSignificandSize - kSignificandSize - 1
        return NumberDiyFp(f, e)
    }

    fun exponent(d64: Long): Int {
        if (isDenormal(d64)) return kDenormalExponent
        val biased_e = (d64 and kExponentMask ushr kSignificandSize and 0xffffffffL).toInt()
        return biased_e - kExponentBias
    }

    fun significand(d64: Long): Long {
        val significand = d64 and kSignificandMask
        return if (!isDenormal(d64)) {
            significand + kHiddenBit
        } else {
            significand
        }
    }

    // Returns true if the double is a denormal.
    fun isDenormal(d64: Long): Boolean {
        return d64 and kExponentMask == 0L
    }

    // We consider denormals not to be special.
    // Hence only Infinity and NaN are special.
    fun isSpecial(d64: Long): Boolean {
        return d64 and kExponentMask == kExponentMask
    }

    fun isNan(d64: Long): Boolean {
        return d64 and kExponentMask == kExponentMask &&
                d64 and kSignificandMask != 0L
    }

    fun isInfinite(d64: Long): Boolean {
        return d64 and kExponentMask == kExponentMask &&
                d64 and kSignificandMask == 0L
    }

    fun sign(d64: Long): Int {
        return if (d64 and kSignMask == 0L) 1 else -1
    }

    // Returns the two boundaries of first argument.
    // The bigger boundary (m_plus) is normalized. The lower boundary has the same
    // exponent as m_plus.
    fun normalizedBoundaries(d64: Long, m_minus: NumberDiyFp, m_plus: NumberDiyFp) {
        val v = asDiyFp(d64)
        val significand_is_zero = v.f() === kHiddenBit
        m_plus.setF((v.f() shl 1) + 1)
        m_plus.setE(v.e() - 1)
        m_plus.normalize()
        if (significand_is_zero && v.e() !== kDenormalExponent) {
            // The boundary is closer. Think of v = 1000e10 and v- = 9999e9.
            // Then the boundary (== (v - v-)/2) is not just at a distance of 1e9 but
            // at a distance of 1e8.
            // The only exception is for the smallest normal: the largest denormal is
            // at the same distance as its successor.
            // Note: denormals have the same exponent as the smallest normals.
            m_minus.setF((v.f() shl 2) - 1)
            m_minus.setE(v.e() - 2)
        } else {
            m_minus.setF((v.f() shl 1) - 1)
            m_minus.setE(v.e() - 1)
        }
        m_minus.setF(m_minus.f() shl m_minus.e() - m_plus.e())
        m_minus.setE(m_plus.e())
    }

    private const val kSignificandSize = 52 // Excludes the hidden bit.
    private const val kExponentBias = 0x3FF + kSignificandSize
    private const val kDenormalExponent = -kExponentBias + 1
}
