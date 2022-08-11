package org.iota.client


enum class LedgerInclusionStateDto(val value: Int) {
    CONFLICTING(0), INCLUDED(1), NO_TRANSACTION(2);

    companion object {
        /*package*/
        fun fromInt(x: Int): LedgerInclusionStateDto {
            return when (x) {
                0 -> CONFLICTING
                1 -> INCLUDED
                2 -> NO_TRANSACTION
                else -> throw Error("Invalid value for enum LedgerInclusionStateDto: $x")
            }
        }
    }
}
