package com.sirius.library.utils.json_canonical

import kotlinx.serialization.builtins.ArraySerializer

internal class NumberDiyFp {
    private var f: Long
    private var e: Int

    constructor() {
        f = 0
        e = 0
    }

    constructor(f: Long, e: Int) {
        this.f = f
        this.e = e
    }

    // this = this - other.
    // The exponents of both numbers must be the same and the significand of this
    // must be bigger than the significand of other.
    // The result will not be normalized.
    fun subtract(other: NumberDiyFp) {
        if(e == other.e){
            if(uint64_gte(f, other.f)){
                f -= other.f
            }else{
                throw  AssertionError()
            }

        }else{
            throw  AssertionError()
        }


    }

    // this = this * other.
    fun multiply(other: NumberDiyFp) {
        // Simply "emulates" a 128 bit multiplication.
        // However: the resulting number only contains 64 bits. The least
        // significant 64 bits are only used for rounding the most significant 64
        // bits.
        val kM32 = 0xFFFFFFFFL
        val a = f ushr 32
        val b = f and kM32
        val c = other.f ushr 32
        val d = other.f and kM32
        val ac = a * c
        val bc = b * c
        val ad = a * d
        val bd = b * d
        var tmp = (bd ushr 32) + (ad and kM32) + (bc and kM32)
        // By adding 1U << 31 to tmp we round the final result.
        // Halfway cases will be round up.
        tmp += 1L shl 31
        val result_f = ac + (ad ushr 32) + (bc ushr 32) + (tmp ushr 32)
        e += other.e + 64
        f = result_f
    }

    fun normalize() {
        if(f != 0L){

        }else{
            throw AssertionError()
        }
        var f = f
        var e = e

        // This method is mainly called for normalizing boundaries. In general
        // boundaries need to be shifted by 10 bits. We thus optimize for this case.
        val k10MSBits = 0xFFC00000L shl 32
        while (f and k10MSBits == 0L) {
            f = f shl 10
            e -= 10
        }
        while (f and kUint64MSB == 0L) {
            f = f shl 1
            e--
        }
        this.f = f
        this.e = e
    }

    fun f(): Long {
        return f
    }

    fun e(): Int {
        return e
    }

    fun setF(new_value: Long) {
        f = new_value
    }

    fun setE(new_value: Int) {
        e = new_value
    }

    override fun toString(): String {
        return "[DiyFp f:$f, e:$e]"
    }

    companion object {
        const val kSignificandSize = 64
        val kUint64MSB = Long.MIN_VALUE
        private fun uint64_gte(a: Long, b: Long): Boolean {
            // greater-or-equal for unsigned int64 in java-style...
            return a == b || (a > b) xor (a < 0) xor (b < 0)
        }

        // Returns a - b.
        // The exponents of both numbers must be the same and this must be bigger
        // than other. The result will not be normalized.
        fun minus(a: NumberDiyFp, b: NumberDiyFp): NumberDiyFp {
            val result = NumberDiyFp(a.f, a.e)
            result.subtract(b)
            return result
        }

        // returns a * b;
        fun times(a: NumberDiyFp, b: NumberDiyFp): NumberDiyFp {
            val result = NumberDiyFp(a.f, a.e)
            result.multiply(b)
            return result
        }

        fun normalize(a: NumberDiyFp): NumberDiyFp {
            val result = NumberDiyFp(a.f, a.e)
            result.normalize()
            return result
        }
    }
}
