package org.iota.client

enum class InputKind(val value: Int) {
    UTXO(0), TREASURY(1);

    companion object {
        /*package*/
        fun fromInt(x: Int): InputKind {
            return when (x) {
                0 -> UTXO
                1 -> TREASURY
                else -> throw Error("Invalid value for enum InputKind: $x")
            }
        }
    }
}
