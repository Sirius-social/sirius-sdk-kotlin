package com.sirius.library.utils.json_canonical

internal object NumberFastDtoa {
    // FastDtoa will produce at most kFastDtoaMaximalLength digits.
    val kFastDtoaMaximalLength = 17

    // The minimal and maximal target exponent define the range of w's binary
    // exponent, where 'w' is the result of multiplying the input by a cached power
    // of ten.
    //
    // A different range might be chosen on a different platform, to optimize digit
    // generation, but a smaller range requires more powers of ten to be cached.
    val minimal_target_exponent = -60
    val maximal_target_exponent = -32

    // Adjusts the last digit of the generated number, and screens out generated
    // solutions that may be inaccurate. A solution may be inaccurate if it is
    // outside the safe interval, or if we ctannot prove that it is closer to the
    // input than a neighboring representation of the same length.
    //
    // Input: * buffer containing the digits of too_high / 10^kappa
    //        * distance_too_high_w == (too_high - w).f() * unit
    //        * unsafe_interval == (too_high - too_low).f() * unit
    //        * rest = (too_high - buffer * 10^kappa).f() * unit
    //        * ten_kappa = 10^kappa * unit
    //        * unit = the common multiplier
    // Output: returns true if the buffer is guaranteed to contain the closest
    //    representable number to the input.
    //  Modifies the generated digits in the buffer to approach (round towards) w.
    fun roundWeed(
        buffer: NumberFastDtoaBuilder,
        distance_too_high_w: Long,
        unsafe_interval: Long,
        rest: Long,
        ten_kappa: Long,
        unit: Long
    ): Boolean {
        var rest = rest
        val small_distance = distance_too_high_w - unit
        val big_distance = distance_too_high_w + unit
        // Let w_low  = too_high - big_distance, and
        //     w_high = too_high - small_distance.
        // Note: w_low < w < w_high
        //
        // The real w (* unit) must lie somewhere inside the interval
        // ]w_low; w_low[ (often written as "(w_low; w_low)")

        // Basically the buffer currently contains a number in the unsafe interval
        // ]too_low; too_high[ with too_low < w < too_high
        //
        //  too_high - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //                     ^v 1 unit            ^      ^                 ^      ^
        //  boundary_high ---------------------     .      .                 .      .
        //                     ^v 1 unit            .      .                 .      .
        //   - - - - - - - - - - - - - - - - - - -  +  - - + - - - - - -     .      .
        //                                          .      .         ^       .      .
        //                                          .  big_distance  .       .      .
        //                                          .      .         .       .    rest
        //                              small_distance     .         .       .      .
        //                                          v      .         .       .      .
        //  w_high - - - - - - - - - - - - - - - - - -     .         .       .      .
        //                     ^v 1 unit                   .         .       .      .
        //  w ----------------------------------------     .         .       .      .
        //                     ^v 1 unit                   v         .       .      .
        //  w_low  - - - - - - - - - - - - - - - - - - - - -         .       .      .
        //                                                           .       .      v
        //  buffer --------------------------------------------------+-------+--------
        //                                                           .       .
        //                                                  safe_interval    .
        //                                                           v       .
        //   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -     .
        //                     ^v 1 unit                                     .
        //  boundary_low -------------------------                     unsafe_interval
        //                     ^v 1 unit                                     v
        //  too_low  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //
        //
        // Note that the value of buffer could lie anywhere inside the range too_low
        // to too_high.
        //
        // boundary_low, boundary_high and w are approximations of the real boundaries
        // and v (the input number). They are guaranteed to be precise up to one unit.
        // In fact the error is guaranteed to be strictly less than one unit.
        //
        // Anything that lies outside the unsafe interval is guaranteed not to round
        // to v when read again.
        // Anything that lies inside the safe interval is guaranteed to round to v
        // when read again.
        // If the number inside the buffer lies inside the unsafe interval but not
        // inside the safe interval then we simply do not know and bail out (returning
        // false).
        //
        // Similarly we have to take into account the imprecision of 'w' when rounding
        // the buffer. If we have two potential representations we need to make sure
        // that the chosen one is closer to w_low and w_high since v can be anywhere
        // between them.
        //
        // By generating the digits of too_high we got the largest (closest to
        // too_high) buffer that is still in the unsafe interval. In the case where
        // w_high < buffer < too_high we try to decrement the buffer.
        // This way the buffer approaches (rounds towards) w.
        // There are 3 conditions that stop the decrementation process:
        //   1) the buffer is already below w_high
        //   2) decrementing the buffer would make it leave the unsafe interval
        //   3) decrementing the buffer would yield a number below w_high and farther
        //      away than the current number. In other words:
        //              (buffer{-1} < w_high) && w_high - buffer{-1} > buffer - w_high
        // Instead of using the buffer directly we use its distance to too_high.
        // Conceptually rest ~= too_high - buffer
        while (rest < small_distance && // Negated condition 1
            unsafe_interval - rest >= ten_kappa &&  // Negated condition 2
            (rest + ten_kappa < small_distance ||  // buffer{-1} > w_high
                    small_distance - rest >= rest + ten_kappa - small_distance)
        ) {
            buffer.decreaseLast()
            rest += ten_kappa
        }

        // We have approached w+ as much as possible. We now test if approaching w-
        // would require changing the buffer. If yes, then we have two possible
        // representations close to w, but we cannot decide which one is closer.
        return if (((rest < big_distance) && (
                    unsafe_interval - rest >= ten_kappa) &&
                    (rest + ten_kappa < big_distance ||
                            big_distance - rest > rest + ten_kappa - big_distance))
        ) {
            false
        } else (2 * unit <= rest) && (rest <= unsafe_interval - 4 * unit)

        // Weeding test.
        //   The safe interval is [too_low + 2 ulp; too_high - 2 ulp]
        //   Since too_low = too_high - unsafe_interval this is equivalent to
        //      [too_high - unsafe_interval + 4 ulp; too_high - 2 ulp]
        //   Conceptually we have: rest ~= too_high - buffer
    }

    val kTen4 = 10000
    val kTen5 = 100000
    val kTen6 = 1000000
    val kTen7 = 10000000
    val kTen8 = 100000000
    val kTen9 = 1000000000

    // Returns the biggest power of ten that is less than or equal than the given
    // number. We furthermore receive the maximum number of bits 'number' has.
    // If number_bits == 0 then 0^-1 is returned
    // The number of bits must be <= 32.
    // Precondition: (1 << number_bits) <= number < (1 << (number_bits + 1)).
    fun biggestPowerTen(
        number: Int,
        number_bits: Int
    ): Long {
        var power: Int
        var exponent: Int
        when (number_bits) {
            32, 31, 30 -> {
                if (kTen9 <= number) {
                    power = kTen9
                    exponent = 9

                } else// else fallthrough
                if (kTen8 <= number) {
                    power = kTen8
                    exponent = 8

                } else// else fallthrough
                if (kTen7 <= number) {
                    power = kTen7
                    exponent = 7

                } else// else fallthrough
                if (kTen6 <= number) {
                    power = kTen6
                    exponent = 6

                } else// else fallthrough
                if (kTen5 <= number) {
                    power = kTen5
                    exponent = 5

                }else // else fallthrough
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                } else// else fallthrough
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else // else fallthrough
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else // else fallthrough
                if (10 <= number) {
                    power = 10
                    exponent = 1

                } else// else fallthrough
                if (1 <= number) {
                    power = 1
                    exponent = 0

                } else{
                    power = 0
                    exponent = -1
                // else fallthrough
                }

            }
            29, 28, 27 -> {
                if (kTen8 <= number) {
                    power = kTen8
                    exponent = 8

                }else
                if (kTen7 <= number) {
                    power = kTen7
                    exponent = 7

                }else
                if (kTen6 <= number) {
                    power = kTen6
                    exponent = 6

                }else
                if (kTen5 <= number) {
                    power = kTen5
                    exponent = 5

                }else
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                }else
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            26, 25, 24 -> {
                if (kTen7 <= number) {
                    power = kTen7
                    exponent = 7

                }else
                if (kTen6 <= number) {
                    power = kTen6
                    exponent = 6

                }else
                if (kTen5 <= number) {
                    power = kTen5
                    exponent = 5

                }else
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                }else
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            23, 22, 21, 20 -> {
                if (kTen6 <= number) {
                    power = kTen6
                    exponent = 6

                }else
                if (kTen5 <= number) {
                    power = kTen5
                    exponent = 5

                }else
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                }else
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            19, 18, 17 -> {
                if (kTen5 <= number) {
                    power = kTen5
                    exponent = 5

                }else
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                }else
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            16, 15, 14 -> {
                if (kTen4 <= number) {
                    power = kTen4
                    exponent = 4

                }else
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            13, 12, 11, 10 -> {
                if (1000 <= number) {
                    power = 1000
                    exponent = 3

                }else
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            9, 8, 7 -> {
                if (100 <= number) {
                    power = 100
                    exponent = 2

                }else
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            6, 5, 4 -> {
                if (10 <= number) {
                    power = 10
                    exponent = 1

                }else
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            3, 2, 1 -> {
                if (1 <= number) {
                    power = 1
                    exponent = 0

                }else{
                    power = 0
                    exponent = -1
                }

            }
            0 -> {
                power = 0
                exponent = -1
            }
            else -> {
                // Following assignments are here to silence compiler warnings.
                power = 0
                exponent = 0
            }
        }
        return power.toLong() shl 32 or (0xffffffffL and exponent.toLong())
    }

    private fun uint64_lte(a: Long, b: Long): Boolean {
        // less-or-equal for unsigned int64 in java-style...
        return a == b || (a < b) xor (a < 0) xor (b < 0)
    }

    // Generates the digits of input number w.
    // w is a floating-point number (DiyFp), consisting of a significand and an
    // exponent. Its exponent is bounded by minimal_target_exponent and
    // maximal_target_exponent.
    //       Hence -60 <= w.e() <= -32.
    //
    // Returns false if it fails, in which case the generated digits in the buffer
    // should not be used.
    // Preconditions:
    //  * low, w and high are correct up to 1 ulp (unit in the last place). That
    //    is, their error must be less that a unit of their last digits.
    //  * low.e() == w.e() == high.e()
    //  * low < w < high, and taking into account their error: low~ <= high~
    //  * minimal_target_exponent <= w.e() <= maximal_target_exponent
    // Postconditions: returns false if procedure fails.
    //   otherwise:
    //     * buffer is not null-terminated, but len contains the number of digits.
    //     * buffer contains the shortest possible decimal digit-sequence
    //       such that LOW < buffer * 10^kappa < HIGH, where LOW and HIGH are the
    //       correct values of low and high (without their error).
    //     * if more than one decimal representation gives the minimal number of
    //       decimal digits then the one closest to W (where W is the correct value
    //       of w) is chosen.
    // Remark: this procedure takes into account the imprecision of its input
    //   numbers. If the precision is not enough to guarantee all the postconditions
    //   then false is returned. This usually happens rarely (~0.5%).
    //
    // Say, for the sake of example, that
    //   w.e() == -48, and w.f() == 0x1234567890abcdef
    // w's value can be computed by w.f() * 2^w.e()
    // We can obtain w's integral digits by simply shifting w.f() by -w.e().
    //  -> w's integral part is 0x1234
    //  w's fractional part is therefore 0x567890abcdef.
    // Printing w's integral part is easy (simply print 0x1234 in decimal).
    // In order to print its fraction we repeatedly multiply the fraction by 10 and
    // get each digit. Example the first digit after the point would be computed by
    //   (0x567890abcdef * 10) >> 48. -> 3
    // The whole thing becomes slightly more complicated because we want to stop
    // once we have enough digits. That is, once the digits inside the buffer
    // represent 'w' we can stop. Everything inside the interval low - high
    // represents w. However we have to pay attention to low, high and w's
    // imprecision.
    fun digitGen(
        low: NumberDiyFp,
        w: NumberDiyFp,
        high: NumberDiyFp,
        buffer: NumberFastDtoaBuilder,
        mk: Int
    ): Boolean {
        if(low.e() === w.e() && w.e() === high.e()){

        }else{
           throw AssertionError()
        }
        if(uint64_lte(low.f() + 1, high.f() - 1)){

        }else{
            throw AssertionError()
        }
        if(minimal_target_exponent <= w.e() && w.e() <= maximal_target_exponent){

        }else{
            throw AssertionError()
        }
        // low, w and high are imprecise, but by less than one ulp (unit in the last
        // place).
        // If we remove (resp. add) 1 ulp from low (resp. high) we are certain that
        // the new numbers are outside of the interval we want the final
        // representation to lie in.
        // Inversely adding (resp. removing) 1 ulp from low (resp. high) would yield
        // numbers that are certain to lie in the interval. We will use this fact
        // later on.
        // We will now start by generating the digits within the uncertain
        // interval. Later we will weed out representations that lie outside the safe
        // interval and thus _might_ lie outside the correct interval.
        var unit: Long = 1
        val too_low = NumberDiyFp(low.f() - unit, low.e())
        val too_high = NumberDiyFp(high.f() + unit, high.e())
        // too_low and too_high are guaranteed to lie outside the interval we want the
        // generated number in.
        val unsafe_interval: NumberDiyFp = NumberDiyFp.minus(too_high, too_low)
        // We now cut the input number into two parts: the integral digits and the
        // fractionals. We will not write any decimal separator though, but adapt
        // kappa instead.
        // Reminder: we are currently computing the digits (stored inside the buffer)
        // such that:   too_low < buffer * 10^kappa < too_high
        // We use too_high for the digit_generation and stop as soon as possible.
        // If we stop early we effectively round down.
        val one = NumberDiyFp(1L shl -w.e(), w.e())
        // Division by one is a shift.
        var integrals = (too_high.f() ushr -one.e() and 0xffffffffL) as Int
        // Modulo by one is an and.
        var fractionals: Long = too_high.f() and one.f() - 1
        val result = biggestPowerTen(integrals, NumberDiyFp.kSignificandSize - -one.e())
        var divider = (result ushr 32 and 0xffffffffL).toInt()
        val divider_exponent = (result and 0xffffffffL).toInt()
        var kappa = divider_exponent + 1
        // Loop invariant: buffer = too_high / 10^kappa  (integer division)
        // The invariant holds for the first iteration: kappa has been initialized
        // with the divider exponent + 1. And the divider is the biggest power of ten
        // that is smaller than integrals.
        while (kappa > 0) {
            val digit = integrals / divider
            buffer.append(('0'.code + digit).toChar())
            integrals %= divider
            kappa--
            // Note that kappa now equals the exponent of the divider and that the
            // invariant thus holds again.
            val rest = (integrals.toLong() shl -one.e()) + fractionals
            // Invariant: too_high = buffer * 10^kappa + DiyFp(rest, one.e())
            // Reminder: unsafe_interval.e() == one.e()
            if (rest < unsafe_interval.f()) {
                // Rounding down (by not emitting the remaining digits) yields a number
                // that lies within the unsafe interval.
                buffer.point = buffer.end - mk + kappa
                return roundWeed(
                    buffer, NumberDiyFp.minus(too_high, w).f(),
                    unsafe_interval.f(), rest,
                    divider.toLong() shl -one.e(), unit
                )
            }
            divider /= 10
        }

        // The integrals have been generated. We are at the point of the decimal
        // separator. In the following loop we simply multiply the remaining digits by
        // 10 and divide by one. We just need to pay attention to multiply associated
        // data (like the interval or 'unit'), too.
        // Instead of multiplying by 10 we multiply by 5 (cheaper operation) and
        // increase its (imaginary) exponent. At the same time we decrease the
        // divider's (one's) exponent and shift its significand.
        // Basically, if fractionals was a DiyFp (with fractionals.e == one.e):
        //      fractionals.f *= 10;
        //      fractionals.f >>= 1; fractionals.e++; // value remains unchanged.
        //      one.f >>= 1; one.e++;                 // value remains unchanged.
        //      and we have again fractionals.e == one.e which allows us to divide
        //           fractionals.f() by one.f()
        // We simply combine the *= 10 and the >>= 1.
        while (true) {
            fractionals *= 5
            unit *= 5
            unsafe_interval.setF(unsafe_interval.f() * 5)
            unsafe_interval.setE(unsafe_interval.e() + 1) // Will be optimized out.
            one.setF(one.f() ushr 1)
            one.setE(one.e() + 1)
            // Integer division by one.
            val digit = (fractionals ushr -one.e() and 0xffffffffL).toInt()
            buffer.append(('0'.code + digit).toChar())
            fractionals = fractionals and one.f() - 1 // Modulo by one.
            kappa--
            if (fractionals < unsafe_interval.f()) {
                buffer.point = buffer.end - mk + kappa
                return roundWeed(
                    buffer, NumberDiyFp.minus(too_high, w).f() * unit,
                    unsafe_interval.f(), fractionals, one.f(), unit
                )
            }
        }
    }

    // Provides a decimal representation of v.
    // Returns true if it succeeds, otherwise the result cannot be trusted.
    // There will be *length digits inside the buffer (not null-terminated).
    // If the function returns true then
    //        v == (double) (buffer * 10^decimal_exponent).
    // The digits in the buffer are the shortest representation possible: no
    // 0.09999999999999999 instead of 0.1. The shorter representation will even be
    // chosen even if the longer one would be closer to v.
    // The last digit will be closest to the actual v. That is, even if several
    // digits might correctly yield 'v' when read again, the closest will be
    // computed.
    fun grisu3(v: Double, buffer: NumberFastDtoaBuilder): Boolean {
        val bits: Long = v.toBits()
        val w: NumberDiyFp = NumberDoubleHelper.asNormalizedDiyFp(bits)
        // boundary_minus and boundary_plus are the boundaries between v and its
        // closest floating-point neighbors. Any number strictly between
        // boundary_minus and boundary_plus will round to v when convert to a double.
        // Grisu3 will never output representations that lie exactly on a boundary.
        val boundary_minus = NumberDiyFp()
        val boundary_plus = NumberDiyFp()
        NumberDoubleHelper.normalizedBoundaries(bits, boundary_minus, boundary_plus)
        if (boundary_plus.e() === w.e()){

        }else{
            throw AssertionError()
        }
        val ten_mk = NumberDiyFp() // Cached power of ten: 10^-k
        val mk: Int = NumberCachedPowers.getCachedPower(
            w.e() + NumberDiyFp.kSignificandSize,
            minimal_target_exponent, maximal_target_exponent, ten_mk
        )
        if(
            minimal_target_exponent <= w.e() + ten_mk.e() +
                    NumberDiyFp.kSignificandSize &&
                    maximal_target_exponent >= w.e() + ten_mk.e() +
                    NumberDiyFp.kSignificandSize
        ){

        }else{
            throw AssertionError()
        }
        // Note that ten_mk is only an approximation of 10^-k. A DiyFp only contains a
        // 64 bit significand and ten_mk is thus only precise up to 64 bits.

        // The DiyFp::Times procedure rounds its result, and ten_mk is approximated
        // too. The variable scaled_w (as well as scaled_boundary_minus/plus) are now
        // off by a small amount.
        // In fact: scaled_w - w*10^k < 1ulp (unit in the last place) of scaled_w.
        // In other words: let f = scaled_w.f() and e = scaled_w.e(), then
        //           (f-1) * 2^e < w*10^k < (f+1) * 2^e
        val scaled_w: NumberDiyFp = NumberDiyFp.times(w, ten_mk)
        if(
            (scaled_w.e() ===
                    boundary_plus.e() + ten_mk.e() + NumberDiyFp.kSignificandSize)
        ){

        }else{
            throw AssertionError()
        }
        // In theory it would be possible to avoid some recomputations by computing
        // the difference between w and boundary_minus/plus (a power of 2) and to
        // compute scaled_boundary_minus/plus by subtracting/adding from
        // scaled_w. However the code becomes much less readable and the speed
        // enhancements are not terriffic.
        val scaled_boundary_minus: NumberDiyFp = NumberDiyFp.times(boundary_minus, ten_mk)
        val scaled_boundary_plus: NumberDiyFp = NumberDiyFp.times(boundary_plus, ten_mk)

        // DigitGen will generate the digits of scaled_w. Therefore we have
        // v == (double) (scaled_w * 10^-mk).
        // Set decimal_exponent == -mk and pass it to DigitGen. If scaled_w is not an
        // integer than it will be updated. For instance if scaled_w == 1.23 then
        // the buffer will be filled with "123" und the decimal_exponent will be
        // decreased by 2.
        return digitGen(
            scaled_boundary_minus, scaled_w, scaled_boundary_plus,
            buffer, mk
        )
    }

    fun dtoa(v: Double, buffer: NumberFastDtoaBuilder): Boolean {
        if((v > 0)){

        }else{
            throw AssertionError()
        }
        if((!v.isNaN())){

        }else{
            throw AssertionError()
        }
        if((!v.isInfinite())){

        }else{
            throw AssertionError()
        }
        return grisu3(v, buffer)
    }

    fun numberToString(v: Double): String? {
        val buffer = NumberFastDtoaBuilder()
        return if (numberToString(v, buffer)) buffer.format() else null
    }

    fun numberToString(v: Double, buffer: NumberFastDtoaBuilder): Boolean {
        var v = v
        buffer.reset()
        if (v < 0) {
            buffer.append('-')
            v = -v
        }
        return dtoa(v, buffer)
    }
}
