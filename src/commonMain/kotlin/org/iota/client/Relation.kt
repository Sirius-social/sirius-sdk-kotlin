package org.iota.client

enum class Relation(val value: Int) {
    KNOWN(0), UNKNOWN(1), AUTOPEERED(2);

    companion object {
        /*package*/
        fun fromInt(x: Int): Relation {
            return when (x) {
                0 -> KNOWN
                1 -> UNKNOWN
                2 -> AUTOPEERED
                else -> throw Error("Invalid value for enum Relation: $x")
            }
        }
    }
}