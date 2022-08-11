package org.iota.client

enum class UnlockBlockKind(val value: Int) {
    ED25519(0), REFERENCE(1);

    companion object {
        /*package*/
        fun fromInt(x: Int): UnlockBlockKind {
            return when (x) {
                0 -> ED25519
                1 -> REFERENCE
                else -> throw Error("Invalid value for enum UnlockBlockKind: $x")
            }
        }
    }
}
