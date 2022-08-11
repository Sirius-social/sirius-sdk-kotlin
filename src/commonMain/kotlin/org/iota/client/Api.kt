package org.iota.client


enum class Api(val value: Int) {
    /**
     * `get_health` API
     */
    GET_HEALTH(0),

    /**
     * `get_info`API
     */
    GET_INFO(1),

    /**
     * `get_peers`API
     */
    GET_PEERS(2),

    /**
     * `get_tips` API
     */
    GET_TIPS(3),

    /**
     * `post_message` API
     */
    POST_MESSAGE(4),

    /**
     * `post_message` API with remote pow
     */
    POST_MESSAGE_WITH_REMOTE_POW(5),

    /**
     * `get_output` API
     */
    GET_OUTPUT(6),

    /**
     * `get_milestone` API
     */
    GET_MILESTONE(7),

    /**
     * `get_message` API
     */
    GET_MESSAGE(8),

    /**
     * `get_balance` API
     */
    GET_BALANCE(9);

    companion object {
        /*package*/
        fun fromInt(x: Int): Api {
            return when (x) {
                0 -> GET_HEALTH
                1 -> GET_INFO
                2 -> GET_PEERS
                3 -> GET_TIPS
                4 -> POST_MESSAGE
                5 -> POST_MESSAGE_WITH_REMOTE_POW
                6 -> GET_OUTPUT
                7 -> GET_MILESTONE
                8 -> GET_MESSAGE
                9 -> GET_BALANCE
                else -> throw Error("Invalid value for enum Api: $x")
            }
        }
    }
}
