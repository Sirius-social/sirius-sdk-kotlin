package org.iota.client

enum class MessagePayloadType(val value: Int) {
    TRANSACTION(0), MILESTONE(1), INDEXATION(2), RECEIPT(3), TREASURY_TRANSACTION(4);

    companion object {
        /*package*/
        fun fromInt(x: Int): MessagePayloadType {
            return when (x) {
                0 -> TRANSACTION
                1 -> MILESTONE
                2 -> INDEXATION
                3 -> RECEIPT
                4 -> TREASURY_TRANSACTION
                else -> throw Error("Invalid value for enum MessagePayloadType: $x")
            }
        }
    }
}
