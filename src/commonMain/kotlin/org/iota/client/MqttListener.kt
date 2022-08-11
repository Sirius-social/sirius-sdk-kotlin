package org.iota.client

interface MqttListener {
    fun onEvent(event: TopicEvent?)
}
