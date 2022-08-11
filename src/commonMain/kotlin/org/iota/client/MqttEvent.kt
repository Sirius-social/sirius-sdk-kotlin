package org.iota.client

enum class MqttEvent(val value: Int) {
    /**
     * Client was connected.
     */
    CONNECTED(0),

    /**
     * Client was disconnected.
     */
    DISCONNECTED(1);

    companion object {
        /*package*/
        fun fromInt(x: Int): MqttEvent {
            return when (x) {
                0 -> CONNECTED
                1 -> DISCONNECTED
                else -> throw Error("Invalid value for enum MqttEvent: $x")
            }
        }
    }
}
