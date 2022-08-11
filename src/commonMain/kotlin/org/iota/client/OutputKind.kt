package org.iota.client

enum class OutputKind(val value: Int) {
    SIGNATURE_LOCKED_SINGLE(0), SIGNATURE_LOCKED_DUST_ALLOWANCE(1), TREASURY(2);

    companion object {
        /*package*/
        fun fromInt(x: Int): OutputKind {
            return when (x) {
                0 -> SIGNATURE_LOCKED_SINGLE
                1 -> SIGNATURE_LOCKED_DUST_ALLOWANCE
                2 -> TREASURY
                else -> throw Error("Invalid value for enum OutputKind: $x")
            }
        }
    }
}
