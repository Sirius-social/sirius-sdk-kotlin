package com.sirius.library.utils.json_canonical

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlin.experimental.and


internal object NumberDToA {
    const val DTOSTR_STANDARD = 0

    /* Either fixed or exponential format; round-trip */
    const val DTOSTR_STANDARD_EXPONENTIAL = 1

    /* Always exponential format; round-trip */
    const val DTOSTR_FIXED = 2

    /* Round to <precision> digits after the decimal point; exponential if number is large */
    const val DTOSTR_EXPONENTIAL = 3

    /* Always exponential format; <precision> significant digits */
    const val DTOSTR_PRECISION =
        4 /* Either fixed or exponential format; <precision> significant digits */
    private const val Frac_mask = 0xfffff
    private const val Exp_shift = 20
    private const val Exp_msk1 = 0x100000
    private const val Bias = 1023
    private const val P = 53
    private const val Exp_shift1 = 20
    private const val Exp_mask = 0x7ff00000
    private const val Bndry_mask = 0xfffff
    private const val Log2P = 1
    private const val Sign_bit = -0x80000000
    private const val Exp_11 = 0x3ff00000
    private const val Ten_pmax = 22
    private const val Quick_max = 14
    private const val Bletch = 0x10
    private const val Frac_mask1 = 0xfffff
    private const val Int_max = 14
    private const val n_bigtens = 5
    private val tens = doubleArrayOf(
        1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9,
        1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19,
        1e20, 1e21, 1e22
    )
    private val bigtens = doubleArrayOf(1e16, 1e32, 1e64, 1e128, 1e256)
    private fun lo0bits(y: Int): Int {
        var k: Int
        var x = y
        if (x and 7 != 0) {
            if (x and 1 != 0) return 0
            return if (x and 2 != 0) {
                1
            } else 2
        }
        k = 0
        if (x and 0xffff == 0) {
            k = 16
            x = x ushr 16
        }
        if (x and 0xff == 0) {
            k += 8
            x = x ushr 8
        }
        if (x and 0xf == 0) {
            k += 4
            x = x ushr 4
        }
        if (x and 0x3 == 0) {
            k += 2
            x = x ushr 2
        }
        if (x and 1 == 0) {
            k++
            x = x ushr 1
            if (x and 1 == 0) return 32
        }
        return k
    }

    /* Return the number (0 through 32) of most significant zero bits in x. */
    private fun hi0bits(x: Int): Int {
        var x = x
        var k = 0
        if (x and -0x10000 == 0) {
            k = 16
            x = x shl 16
        }
        if (x and -0x1000000 == 0) {
            k += 8
            x = x shl 8
        }
        if (x and -0x10000000 == 0) {
            k += 4
            x = x shl 4
        }
        if (x and -0x40000000 == 0) {
            k += 2
            x = x shl 2
        }
        if (x and -0x80000000 == 0) {
            k++
            if (x and 0x40000000 == 0) return 32
        }
        return k
    }

    private fun stuffBits(bits: ByteArray, offset: Int, `val`: Int) {
        bits[offset] = (`val` shr 24).toByte()
        bits[offset + 1] = (`val` shr 16).toByte()
        bits[offset + 2] = (`val` shr 8).toByte()
        bits[offset + 3] = `val`.toByte()
    }

    /* Convert d into the form b*2^e, where b is an odd integer.  b is the returned
     * Bigint and e is the returned binary exponent.  Return the number of significant
     * bits in b in bits.  d must be finite and nonzero. */
    private fun d2b(d: Double, e: IntArray, bits: IntArray): BigInteger {
        val dbl_bits: ByteArray
        val i: Int
        var k: Int
        var y: Int
        var z: Int
        var de: Int
        val dBits: Long = d.toBits()
        var d0 = (dBits ushr 32).toInt()
        val d1 = dBits.toInt()
        z = d0 and Frac_mask
        d0 = d0 and 0x7fffffff /* clear sign bit, which we ignore */
        if ((d0 ushr Exp_shift).also { de = it } != 0) z = z or Exp_msk1
        if (d1.also { y = it } != 0) {
            dbl_bits = ByteArray(8)
            k = lo0bits(y)
            y = y ushr k
            if (k != 0) {
                stuffBits(dbl_bits, 4, y or z shl 32 - k)
                z = z shr k
            } else stuffBits(dbl_bits, 4, y)
            stuffBits(dbl_bits, 0, z)
            i = if (z != 0) 2 else 1
        } else {
            //        JS_ASSERT(z);
            dbl_bits = ByteArray(4)
            k = lo0bits(z)
            z = z ushr k
            stuffBits(dbl_bits, 0, z)
            k += 32
            i = 1
        }
        if (de != 0) {
            e[0] = de - Bias - (P - 1) + k
            bits[0] = P - k
        } else {
            e[0] = de - Bias - (P - 1) + 1 + k
            bits[0] = 32 * i - hi0bits(z)
        }
        return BigInteger.fromByteArray(dbl_bits, Sign.ZERO)
    }

    /* dtoa for IEEE arithmetic (dmg): convert double to ASCII string.
     *
     * Inspired by "How to Print Floating-Point Numbers Accurately" by
     * Guy L. Steele, Jr. and Jon L. White [Proc. ACM SIGPLAN '90, pp. 92-101].
     *
     * Modifications:
     *  1. Rather than iterating, we use a simple numeric overestimate
     *     to determine k = floor(log10(d)).  We scale relevant
     *     quantities using O(log2(k)) rather than O(k) multiplications.
     *  2. For some modes > 2 (corresponding to ecvt and fcvt), we don't
     *     try to generate digits strictly left to right.  Instead, we
     *     compute with fewer bits and propagate the carry if necessary
     *     when rounding the final digit up.  This is often faster.
     *  3. Under the assumption that input will be rounded nearest,
     *     mode 0 renders 1e23 as 1e23 rather than 9.999999999999999e22.
     *     That is, we allow equality in stopping tests when the
     *     round-nearest rule will give the same floating-point value
     *     as would satisfaction of the stopping test with strict
     *     inequality.
     *  4. We remove common factors of powers of 2 from relevant
     *     quantities.
     *  5. When converting floating-point integers less than 1e16,
     *     we use floating-point arithmetic rather than resorting
     *     to multiple-precision integers.
     *  6. When asked to produce fewer than 15 digits, we first try
     *     to get by with floating-point arithmetic; we resort to
     *     multiple-precision integer arithmetic only if we cannot
     *     guarantee that the floating-point calculation has given
     *     the correctly rounded result.  For k requested digits and
     *     "uniformly" distributed input, the probability is
     *     something like 10^(k-15) that we must resort to the Long
     *     calculation.
     */
    fun word0(d: Double): Int {
        val dBits: Long = d.toBits()
        return (dBits shr 32).toInt()
    }

    fun setWord0(d: Double, i: Int): Double {
        var dBits: Long = d.toBits()
        dBits = i.toLong() shl 32 or (dBits and 0x0FFFFFFFFL)
        return Double.fromBits(dBits)
    }

    fun word1(d: Double): Int {
        val dBits: Long = d.toBits()
        return dBits.toInt()
    }

    /* Return b * 5^k.  k must be nonnegative. */ // XXXX the C version built a cache of these
    fun pow5mult(b: BigInteger?, k: Int): BigInteger {
        return b?.multiply(BigInteger.fromInt(5).pow(k)) ?: BigInteger(0)
    }

    fun roundOff(buf: StringBuilder): Boolean {
        var i: Int = buf.length
        while (i != 0) {
            --i
            val c: Char = buf.get(i)
            if (c != '9') {
                buf.set(i, (c.code + 1).toChar())
                buf.setLength(i + 1)
                return false
            }
        }
        buf.setLength(0)
        return true
    }

    /* Always emits at least one digit. */ /* If biasUp is set, then rounding in modes 2 and 3 will round away from zero
     * when the number is exactly halfway between two representable values.  For example,
     * rounding 2.5 to zero digits after the decimal point will return 3 and not 2.
     * 2.49 will still round to 2, and 2.51 will still round to 3. */
    /* bufsize should be at least 20 for modes 0 and 1.  For the other modes,
     * bufsize should be two greater than the maximum number of output characters expected. */
    fun JS_dtoa(
        d: Double, mode: Int, biasUp: Boolean, ndigits: Int,
        sign: BooleanArray, buf: StringBuilder
    ): Int {
        /*  Arguments ndigits, decpt, sign are similar to those
            of ecvt and fcvt; trailing zeros are suppressed from
            the returned string.  If not null, *rve is set to point
            to the end of the return value.  If d is +-Infinity or NaN,
            then *decpt is set to 9999.

            mode:
            0 ==> shortest string that yields d when read in
            and rounded to nearest.
            1 ==> like 0, but with Steele & White stopping rule;
            e.g. with IEEE P754 arithmetic , mode 0 gives
            1e23 whereas mode 1 gives 9.999999999999999e22.
            2 ==> max(1,ndigits) significant digits.  This gives a
            return value similar to that of ecvt, except
            that trailing zeros are suppressed.
            3 ==> through ndigits past the decimal point.  This
            gives a return value similar to that from fcvt,
            except that trailing zeros are suppressed, and
            ndigits can be negative.
            4-9 should give the same return values as 2-3, i.e.,
            4 <= mode <= 9 ==> same return as mode
            2 + (mode & 1).  These modes are mainly for
            debugging; often they run slower but sometimes
            faster than modes 2-3.
            4,5,8,9 ==> left-to-right digit generation.
            6-9 ==> don't try fast floating-point estimate
            (if applicable).

            Values of mode other than 0-9 are treated as mode 0.

            Sufficient space is allocated to the return value
            to hold the suppressed trailing zeros.
        */
        var d = d
        var mode = mode
        var ndigits = ndigits
        var b2: Int
        var b5: Int
        var i: Int
        var ieps: Int
        var ilim: Int
        val ilim0: Int
        var ilim1: Int
        var j: Int
        var j1: Int
        var k: Int
        val k0: Int
        var m2: Int
        var m5: Int
        var s2: Int
        var s5: Int
        var dig: Char
        var L: Long
        val x: Long
        var b: BigInteger
        val b1: BigInteger
        var delta: BigInteger
        var mlo: BigInteger?
        var mhi: BigInteger?
        var S: BigInteger?
        val be = IntArray(1)
        val bbits = IntArray(1)
        var d2: Double
        var ds: Double
        var eps: Double
        var spec_case: Boolean
        val denorm: Boolean
        var k_check: Boolean
        var try_quick: Boolean
        var leftright: Boolean
        if (word0(d) and Sign_bit != 0) {
            /* set sign for everything, including 0's and NaNs */
            sign[0] = true
            // word0(d) &= ~Sign_bit;  /* clear sign bit */
            d = setWord0(d, word0(d) and Sign_bit.inv())
        } else sign[0] = false
        if (word0(d) and Exp_mask == Exp_mask) {
            /* Infinity or NaN */
            buf.append(if (word1(d) == 0 && word0(d) and Frac_mask == 0) "Infinity" else "NaN")
            return 9999
        }
        if (d == 0.0) {
//          no_digits:
            buf.setLength(0)
            buf.append('0') /* copy "0" to buffer */
            return 1
        }
        b = d2b(d, be, bbits)
        if ((word0(d) ushr Exp_shift1 and (Exp_mask shr Exp_shift1)).also {
                i = it
            } != 0) {
            d2 = setWord0(d, word0(d) and Frac_mask1 or Exp_11)
            /* log(x)   ~=~ log(1.5) + (x-1.5)/1.5
             * log10(x)  =  log(x) / log(10)
             *      ~=~ log(1.5)/log(10) + (x-1.5)/(1.5*log(10))
             * log10(d) = (i-Bias)*log(2)/log(10) + log10(d2)
             *
             * This suggests computing an approximation k to log10(d) by
             *
             * k = (i - Bias)*0.301029995663981
             *  + ( (d2-1.5)*0.289529654602168 + 0.176091259055681 );
             *
             * We want k to be too large rather than too small.
             * The error in the first-order Taylor series approximation
             * is in our favor, so we just round up the constant enough
             * to compensate for any error in the multiplication of
             * (i - Bias) by 0.301029995663981; since |i - Bias| <= 1077,
             * and 1077 * 0.30103 * 2^-52 ~=~ 7.2e-14,
             * adding 1e-13 to the constant term more than suffices.
             * Hence we adjust the constant term to 0.1760912590558.
             * (We could get a more accurate k by invoking log10,
             *  but this is probably not worthwhile.)
             */i -= Bias
            denorm = false
        } else {
            /* d is denormalized */
            i = bbits[0] + be[0] + (Bias + (P - 1) - 1)
            x =
                if (i > 32) word0(d).toLong() shl 64 - i or word1(d).toLong() ushr i - 32 else word1(
                    d
                ).toLong() shl 32 - i
            //            d2 = x;
//            word0(d2) -= 31*Exp_msk1; /* adjust exponent */
            d2 = setWord0(x.toDouble(), word0(x.toDouble()) - 31 * Exp_msk1)
            i -= Bias + (P - 1) - 1 + 1
            denorm = true
        }
        /* At this point d = f*2^i, where 1 <= f < 2.  d2 is an approximation of f. */ds =
            (d2 - 1.5) * 0.289529654602168 + 0.1760912590558 + i * 0.301029995663981
        k = ds.toInt()
        if (ds < 0.0 && ds != k.toDouble()) k-- /* want k = floor(ds) */
        k_check = true
        if (k >= 0 && k <= Ten_pmax) {
            if (d < tens[k]) k--
            k_check = false
        }
        /* At this point floor(log10(d)) <= k <= floor(log10(d))+1.
           If k_check is zero, we're guaranteed that k = floor(log10(d)). */j = bbits[0] - i - 1
        /* At this point d = b/2^j, where b is an odd integer. */if (j >= 0) {
            b2 = 0
            s2 = j
        } else {
            b2 = -j
            s2 = 0
        }
        if (k >= 0) {
            b5 = 0
            s5 = k
            s2 += k
        } else {
            b2 -= k
            b5 = -k
            s5 = 0
        }
        /* At this point d/10^k = (b * 2^b2 * 5^b5) / (2^s2 * 5^s5), where b is an odd integer,
           b2 >= 0, b5 >= 0, s2 >= 0, and s5 >= 0. */if (mode < 0 || mode > 9) mode = 0
        try_quick = true
        if (mode > 5) {
            mode -= 4
            try_quick = false
        }
        leftright = true
        ilim1 = 0
        ilim = ilim1
        when (mode) {
            0, 1 -> {
                run {
                    ilim1 = -1
                    ilim = ilim1
                }
                i = 18
                ndigits = 0
            }
            2 -> {
                leftright = false
                if (ndigits <= 0) ndigits = 1
                run {
                    i = ndigits
                    ilim1 = i
                    ilim = ilim1
                }
            }
            4 -> {
                if (ndigits <= 0) ndigits = 1
                run {
                    i = ndigits
                    ilim1 = i
                    ilim = ilim1
                }
            }
            3 -> {
                leftright = false
                i = ndigits + k + 1
                ilim = i
                ilim1 = i - 1
                if (i <= 0) i = 1
            }
            5 -> {
                i = ndigits + k + 1
                ilim = i
                ilim1 = i - 1
                if (i <= 0) i = 1
            }
        }
        /* ilim is the maximum number of significant digits we want, based on k and ndigits. */
        /* ilim1 is the maximum number of significant digits we want, based on k and ndigits,
           when it turns out that k was computed too high by one. */
        var fast_failed = false
        if (ilim >= 0 && ilim <= Quick_max && try_quick) {

            /* Try to get by with floating-point arithmetic. */
            i = 0
            d2 = d
            k0 = k
            ilim0 = ilim
            ieps = 2 /* conservative */
            /* Divide d by 10^k, keeping track of the roundoff error and avoiding overflows. */if (k > 0) {
                ds = tens[k and 0xf]
                j = k shr 4
                if (j and Bletch != 0) {
                    /* prevent overflows */
                    j = j and Bletch - 1
                    d /= bigtens[n_bigtens - 1]
                    ieps++
                }
                while (j != 0) {
                    if (j and 1 != 0) {
                        ieps++
                        ds *= bigtens[i]
                    }
                    j = j shr 1
                    i++
                }
                d /= ds
            } else if (-k.also { j1 = it } != 0) {
                d *= tens[j1 and 0xf]
                j = j1 shr 4
                while (j != 0) {
                    if (j and 1 != 0) {
                        ieps++
                        d *= bigtens[i]
                    }
                    j = j shr 1
                    i++
                }
            }
            /* Check that k was computed correctly. */if (k_check && d < 1.0 && ilim > 0) {
                if (ilim1 <= 0) fast_failed = true else {
                    ilim = ilim1
                    k--
                    d *= 10.0
                    ieps++
                }
            }
            /* eps bounds the cumulative error. */
//            eps = ieps*d + 7.0;
//            word0(eps) -= (P-1)*Exp_msk1;
            eps = ieps * d + 7.0
            eps = setWord0(eps, word0(eps) - (P - 1) * Exp_msk1)
            if (ilim == 0) {
                mhi = null
                S = mhi
                d -= 5.0
                if (d > eps) {
                    buf.append('1')
                    k++
                    return k + 1
                }
                if (d < -eps) {
                    buf.setLength(0)
                    buf.append('0') /* copy "0" to buffer */
                    return 1
                }
                fast_failed = true
            }
            if (!fast_failed) {
                fast_failed = true
                if (leftright) {
                    /* Use Steele & White method of only
                     * generating digits needed.
                     */
                    eps = 0.5 / tens[ilim - 1] - eps
                    i = 0
                    while (true) {
                        L = d.toLong()
                        d -= L.toDouble()
                        buf.append(Char(('0'.code.toLong() + L).toUShort()))
                        if (d < eps) {
                            return k + 1
                        }
                        if (1.0 - d < eps) {
//                            goto bump_up;
                            var lastCh: Char
                            while (true) {
                                lastCh = buf.get(buf.length - 1)
                                buf.setLength(buf.length - 1)
                                if (lastCh != '9') break
                                if (buf.length == 0) {
                                    k++
                                    lastCh = '0'
                                    break
                                }
                            }
                            buf.append((lastCh.code + 1).toChar())
                            return k + 1
                        }
                        if (++i >= ilim) break
                        eps *= 10.0
                        d *= 10.0
                    }
                } else {
                    /* Generate ilim digits, then fix them up. */
                    eps *= tens[ilim - 1]
                    i = 1
                    while (true) {
                        L = d.toLong()
                        d -= L.toDouble()
                        buf.append(Char(('0'.code.toLong() + L).toUShort()))
                        if (i == ilim) {
                            if (d > 0.5 + eps) {
//                                goto bump_up;
                                var lastCh: Char
                                while (true) {
                                    lastCh = buf.get(buf.length - 1)
                                    buf.setLength(buf.length - 1)
                                    if (lastCh != '9') break
                                    if (buf.length == 0) {
                                        k++
                                        lastCh = '0'
                                        break
                                    }
                                }
                                buf.append((lastCh.code + 1).toChar())
                                return k + 1
                            } else if (d < 0.5 - eps) {
                                stripTrailingZeroes(buf)
                                //                                    while(*--s == '0') ;
//                                    s++;
                                return k + 1
                            }
                            break
                        }
                        i++
                        d *= 10.0
                    }
                }
            }
            if (fast_failed) {
                buf.setLength(0)
                d = d2
                k = k0
                ilim = ilim0
            }
        }

        /* Do we have a "small" integer? */if (be[0] >= 0 && k <= Int_max) {
            /* Yes. */
            ds = tens[k]
            if (ndigits < 0 && ilim <= 0) {
                mhi = null
                S = mhi
                if (ilim < 0 || d < 5 * ds || !biasUp && d == 5 * ds) {
                    buf.setLength(0)
                    buf.append('0') /* copy "0" to buffer */
                    return 1
                }
                buf.append('1')
                k++
                return k + 1
            }
            i = 1
            while (true) {
                L = (d / ds).toLong()
                d -= L * ds
                buf.append(Char(('0'.code.toLong() + L).toUShort()))
                if (i == ilim) {
                    d += d
                    if (d > ds || d == ds && (L and 1 != 0L || biasUp)) {
//                    bump_up:
//                        while(*--s == '9')
//                            if (s == buf) {
//                                k++;
//                                *s = '0';
//                                break;
//                            }
//                        ++*s++;
                        var lastCh: Char
                        while (true) {
                            lastCh = buf.get(buf.length - 1)
                            buf.setLength(buf.length - 1)
                            if (lastCh != '9') break
                            if (buf.length == 0) {
                                k++
                                lastCh = '0'
                                break
                            }
                        }
                        buf.append((lastCh.code + 1).toChar())
                    }
                    break
                }
                d *= 10.0
                if (d == 0.0) break
                i++
            }
            return k + 1
        }
        m2 = b2
        m5 = b5
        mlo = null
        mhi = mlo
        if (leftright) {
            if (mode < 2) {
                i = if (denorm) be[0] + (Bias + (P - 1) - 1 + 1) else 1 + P - bbits[0]
                /* i is 1 plus the number of trailing zero bits in d's significand. Thus,
                   (2^m2 * 5^m5) / (2^(s2+i) * 5^s5) = (1/2 lsb of d)/10^k. */
            } else {
                j = ilim - 1
                if (m5 >= j) m5 -= j else {
                    j -= m5
                    s5 += j
                    b5 += j
                    m5 = 0
                }
                if (ilim.also { i = it } < 0) {
                    m2 -= i
                    i = 0
                }
                /* (2^m2 * 5^m5) / (2^(s2+i) * 5^s5) = (1/2 * 10^(1-ilim))/10^k. */
            }
            b2 += i
            s2 += i
            mhi = BigInteger.fromInt(1)
            /* (mhi * 2^m2 * 5^m5) / (2^s2 * 5^s5) = one-half of last printed (when mode >= 2) or
               input (when mode < 2) significant digit, divided by 10^k. */
        }
        /* We still have d/10^k = (b * 2^b2 * 5^b5) / (2^s2 * 5^s5).  Reduce common factors in
           b2, m2, and s2 without changing the equalities. */if (m2 > 0 && s2 > 0) {
            i = if (m2 < s2) m2 else s2
            b2 -= i
            m2 -= i
            s2 -= i
        }

        /* Fold b5 into b and m5 into mhi. */if (b5 > 0) {
            if (leftright) {
                if (m5 > 0) {
                    mhi = pow5mult(mhi, m5)
                    b1 = mhi.multiply(b)
                    b = b1
                }
                if (b5 - m5.also { j = it } != 0) b = pow5mult(b, j)
            } else b = pow5mult(b, b5)
        }
        /* Now we have d/10^k = (b * 2^b2) / (2^s2 * 5^s5) and
           (mhi * 2^m2) / (2^s2 * 5^s5) = one-half of last printed or input significant digit, divided by 10^k. */S =
            BigInteger.fromInt(1)
        if (s5 > 0) S = pow5mult(S, s5)
        /* Now we have d/10^k = (b * 2^b2) / (S * 2^s2) and
           (mhi * 2^m2) / (S * 2^s2) = one-half of last printed or input significant digit, divided by 10^k. */

        /* Check for special case that d is a normalized power of 2. */spec_case = false
        if (mode < 2) {
            if (word1(d) == 0 && word0(d) and Bndry_mask == 0
                && word0(d) and (Exp_mask and Exp_mask shl 1) != 0
            ) {
                /* The special case.  Here we want to be within a quarter of the last input
                   significant digit instead of one half of it when the decimal output string's value is less than d.  */
                b2 += Log2P
                s2 += Log2P
                spec_case = true
            }
        }

        /* Arrange for convenient computation of quotients:
         * shift left if necessary so divisor has 4 leading 0 bits.
         *
         * Perhaps we should just compute leading 28 bits of S once
         * and for all and pass them and a shift to quorem, so it
         * can do shifts and ors to compute the numerator for q.
         */
        val S_bytes: ByteArray = S?.toByteArray() ?: ByteArray(0)
        var S_hiWord = 0
        for (idx in 0..3) {
            S_hiWord = S_hiWord shl 8
            if (idx < S_bytes.size) S_hiWord = S_hiWord or (S_bytes[idx] and 0xFF.toByte()).toInt()
        }
        if ((if (s5 != 0) 32 - hi0bits(S_hiWord) else 1) + s2 and 0x1f.also {
                i = it
            } != 0) i = 32 - i
        /* i is the number of leading zero bits in the most significant word of S*2^s2. */if (i > 4) {
            i -= 4
            b2 += i
            m2 += i
            s2 += i
        } else if (i < 4) {
            i += 28
            b2 += i
            m2 += i
            s2 += i
        }
        /* Now S*2^s2 has exactly four leading zero bits in its most significant word. */if (b2 > 0) b =
            b.shl(b2)
        if (s2 > 0) S = S.shl(s2)
        /* Now we have d/10^k = b/S and
           (mhi * 2^m2) / S = maximum acceptable error, divided by 10^k. */if (k_check) {
            if (b.compareTo(S) < 0) {
                k--
                b = b.multiply(BigInteger.fromInt(10)) /* we botched the k estimate */
                if (leftright) mhi = mhi?.multiply(BigInteger.fromInt(10))
                ilim = ilim1
            }
        }
        /* At this point 1 <= d/10^k = b/S < 10. */if (ilim <= 0 && mode > 2) {
            /* We're doing fixed-mode output and d is less than the minimum nonzero output in this mode.
               Output either zero or the minimum nonzero output depending on which is closer to d. */
            if (ilim < 0
                || b.compareTo(S.multiply(BigInteger.fromInt(5)).also { S = it }).also {
                    i = it
                } < 0
                || i == 0 && !biasUp
            ) {
                /* Always emit at least one digit.  If the number appears to be zero
               using the current mode, then emit one '0' digit and set decpt to 1. */
                /*no_digits:
                k = -1 - ndigits;
                goto ret; */
                buf.setLength(0)
                buf.append('0') /* copy "0" to buffer */
                return 1
                //                goto no_digits;
            }
            //        one_digit:
            buf.append('1')
            k++
            return k + 1
        }
        if (leftright) {
            if (m2 > 0) mhi = mhi?.shl(m2)

            /* Compute mlo -- check for special case
             * that d is a normalized power of 2.
             */mlo = mhi
            if (spec_case) {
                mhi = mlo
                mhi = mhi?.shl(Log2P)
            }
            /* mlo/S = maximum acceptable error, divided by 10^k, if the output is less than d. */
            /* mhi/S = maximum acceptable error, divided by 10^k, if the output is greater than d. */i =
                1
            while (true) {
                val divResult: Pair<BigInteger, BigInteger> = b.divideAndRemainder(S?: BigInteger(0))
                b = divResult.second
                dig = (divResult.first.intValue() + '0'.code).toChar()
                /* Do we yet have the shortest decimal string
                 * that will round to d?
                 */j = b.compareTo(mlo?: BigInteger(0))
                /* j is b/S compared with mlo/S. */delta = S?.subtract(mhi?: BigInteger(0)) ?: BigInteger(0)
                j1 = if (delta.signum() <= 0) 1 else b.compareTo(delta)
                /* j1 is b/S compared with 1 - mhi/S. */if (j1 == 0 && mode == 0 && word1(d) and 1 == 0) {
                    if (dig == '9') {
                        buf.append('9')
                        if (roundOff(buf)) {
                            k++
                            buf.append('1')
                        }
                        return k + 1
                        //                        goto round_9_up;
                    }
                    if (j > 0) dig++
                    buf.append(dig)
                    return k + 1
                }
                if (j < 0
                    || (j == 0
                            && mode == 0
                            && word1(d) and 1 == 0)
                ) {
                    if (j1 > 0) {
                        /* Either dig or dig+1 would work here as the least significant decimal digit.
                           Use whichever would produce a decimal value closer to d. */
                        b = b.shl(1)
                        j1 = b.compareTo(S?:BigInteger(0))
                        if ((j1 > 0 || j1 == 0 && (dig.code and 1 == 1 || biasUp))
                            && dig++ == '9'
                        ) {
                            buf.append('9')
                            if (roundOff(buf)) {
                                k++
                                buf.append('1')
                            }
                            return k + 1
                            //                                goto round_9_up;
                        }
                    }
                    buf.append(dig)
                    return k + 1
                }
                if (j1 > 0) {
                    if (dig == '9') { /* possible if i == 1 */
//                    round_9_up:
//                        *s++ = '9';
//                        goto roundoff;
                        buf.append('9')
                        if (roundOff(buf)) {
                            k++
                            buf.append('1')
                        }
                        return k + 1
                    }
                    buf.append((dig.code + 1).toChar())
                    return k + 1
                }
                buf.append(dig)
                if (i == ilim) break
                b = b.multiply(BigInteger.fromInt(10))
                if (mlo === mhi) {
                    mhi = mhi?.multiply(BigInteger.fromInt(10))
                    mlo = mhi
                } else {
                    mlo = mlo?.multiply(BigInteger.fromInt(10))
                    mhi = mhi?.multiply(BigInteger.fromInt(10))
                }
                i++
            }
        } else {
            i = 1
            while (true) {

//                (char)(dig = quorem(b,S) + '0');
                val divResult: Pair<BigInteger, BigInteger> = b.divideAndRemainder(S?: BigInteger(0))
                b = divResult.second
                dig = (divResult.first.intValue() + '0'.code).toChar()
                buf.append(dig)
                if (i >= ilim) break
                b = b.multiply(BigInteger.fromInt(10))
                i++
            }
        }

        /* Round off last digit */b = b.shl(1)
        j = b.compareTo(S?:BigInteger(0))
        if (j > 0 || j == 0 && (dig.code and 1 == 1 || biasUp)) {
//        roundoff:
//            while(*--s == '9')
//                if (s == buf) {
//                    k++;
//                    *s++ = '1';
//                    goto ret;
//                }
//            ++*s++;
            if (roundOff(buf)) {
                k++
                buf.append('1')
                return k + 1
            }
        } else {
            stripTrailingZeroes(buf)
            //            while(*--s == '0') ;
//            s++;
        }
        //      ret:
//        Bfree(S);
//        if (mhi) {
//            if (mlo && mlo != mhi)
//                Bfree(mlo);
//            Bfree(mhi);
//        }
//      ret1:
//        Bfree(b);
//        JS_ASSERT(s < buf + bufsize);
        return k + 1
    }

    private fun stripTrailingZeroes(buf: StringBuilder) {
//      while(*--s == '0') ;
//      s++;
        var bl: Int = buf.length
        while (bl-- > 0 && buf.get(bl) == '0') {
            // empty
        }
        buf.setLength(bl + 1)
    }

    /* Mapping of JSDToStrMode -> JS_dtoa mode */
    private val dtoaModes = intArrayOf(
        0,  /* DTOSTR_STANDARD */
        0,  /* DTOSTR_STANDARD_EXPONENTIAL, */
        3,  /* DTOSTR_FIXED, */
        2,  /* DTOSTR_EXPONENTIAL, */
        2
    ) /* DTOSTR_PRECISION */

    fun JS_dtostr(buffer: StringBuilder, mode: Int, precision: Int, d: Double) {
        var mode = mode
        val decPt: Int /* Position of decimal point relative to first digit returned by JS_dtoa */
        val sign = BooleanArray(1) /* true if the sign bit was set in d */
        var nDigits: Int /* Number of significand digits returned by JS_dtoa */

//        JS_ASSERT(bufferSize >= (size_t)(mode <= DTOSTR_STANDARD_EXPONENTIAL ? DTOSTR_STANDARD_BUFFER_SIZE :
//                DTOSTR_VARIABLE_BUFFER_SIZE(precision)));
        if (mode == DTOSTR_FIXED && (d >= 1e21 || d <= -1e21)) mode =
            DTOSTR_STANDARD /* Change mode here rather than below because the buffer may not be large enough to hold a large integer. */
        decPt = JS_dtoa(
            d,
            dtoaModes[mode], mode >= DTOSTR_FIXED, precision, sign, buffer
        )
        nDigits = buffer.length

        /* If Infinity, -Infinity, or NaN, return the string regardless of the mode. */if (decPt != 9999) {
            var exponentialNotation = false
            var minNDigits =
                0 /* Minimum number of significand digits required by mode and precision */
            val p: Int
            when (mode) {
                DTOSTR_STANDARD -> if (decPt < -5 || decPt > 21) exponentialNotation =
                    true else minNDigits = decPt
                DTOSTR_FIXED -> minNDigits =
                    if (precision >= 0) decPt + precision else decPt
                DTOSTR_EXPONENTIAL -> {
                    //                    JS_ASSERT(precision > 0);
                    minNDigits = precision
                    exponentialNotation = true
                }
                DTOSTR_STANDARD_EXPONENTIAL -> exponentialNotation = true
                DTOSTR_PRECISION -> {
                    //                    JS_ASSERT(precision > 0);
                    minNDigits = precision
                    if (decPt < -5 || decPt > precision) exponentialNotation = true
                }
            }

            /* If the number has fewer than minNDigits, pad it with zeros at the end */if (nDigits < minNDigits) {
                p = minNDigits
                nDigits = minNDigits
                do {
                    buffer.append('0')
                } while (buffer.length != p)
            }
            if (exponentialNotation) {
                /* Insert a decimal point if more than one significand digit */
                if (nDigits != 1) {
                    buffer.insert(1, '.')
                }
                buffer.append('e')
                if (decPt - 1 >= 0) buffer.append('+')
                buffer.append(decPt - 1)
                //                JS_snprintf(numEnd, bufferSize - (numEnd - buffer), "e%+d", decPt-1);
            } else if (decPt != nDigits) {
                /* Some kind of a fraction in fixed notation */
//                JS_ASSERT(decPt <= nDigits);
                if (decPt > 0) {
                    /* dd...dd . dd...dd */
                    buffer.insert(decPt, '.')
                } else {
                    /* 0 . 00...00dd...dd */
                    for (i in 0 until 1 - decPt) buffer.insert(0, '0')
                    buffer.insert(1, '.')
                }
            }
        }

        /* If negative and neither -0.0 nor NaN, output a leading '-'. */if (sign[0] &&
            !(word0(d) == Sign_bit && word1(d) == 0) &&
            !(word0(d) and Exp_mask == Exp_mask &&
                    (word1(d) != 0 || word0(d) and Frac_mask != 0))
        ) {
            buffer.insert(0, '-')
        }
    }
}

