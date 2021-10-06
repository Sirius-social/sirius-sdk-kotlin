package com.sirius.library

import com.sirius.library.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Pong
import com.sirius.library.messaging.Message
import com.sirius.library.models.TestMessage1
import com.sirius.library.models.TestMessage2
import com.sirius.library.utils.JSONObject
import kotlin.test.*

class TestMessages {
    @Test
    fun testRegisterProtocolMessageSuccess() {
        try {
            Message.registerMessageClass(TestMessage1::class, "test-protocol")
            val messObject: JSONObject = JSONObject()
            messObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/test-protocol/1.0/name")
            val (first, second) = Message.restoreMessageInstance(messObject.toString())
            assertTrue(first)
            assertTrue(second is TestMessage1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testRegisterProtocolMessageFail() {
        Message.registerMessageClass(TestMessage1::class, "test-protocol")
        val messObject: JSONObject = JSONObject()
        messObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/fake-protocol/1.0/name")
        try {
            val (first, second) = Message.restoreMessageInstance(messObject.toString())
            assertFalse(first)
            assertNull(second)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testRegisterProtocolMessageMultipleName() {
        Message.registerMessageClass(TestMessage1::class, "test-protocol")
        Message.registerMessageClass(TestMessage2::class, "test-protocol", "test-name")
        try {
            val messObject: JSONObject = JSONObject()
            messObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/test-protocol/1.0/name")
            val (first, second) = Message.restoreMessageInstance(messObject.toString())
            assertTrue(first)
            assertTrue(second is TestMessage1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val messObject: JSONObject = JSONObject()
            messObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/test-protocol/1.0/test-name")
            val (first, second) = Message.restoreMessageInstance(messObject.toString())
           assertTrue(first)
            assertTrue(second is TestMessage2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testAriesPingPong() {
        val pingObject: JSONObject = JSONObject()
        pingObject.put("@id", "trust-ping-message-id")
        pingObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/trust_ping/1.0/ping")
        pingObject.put("comment", "Hi. Are you OK?")
        pingObject.put("response_requested", true)
        val ping = Ping(pingObject.toString())
        try {
            val (first, second) = Message.restoreMessageInstance(pingObject.toString())
            assertTrue(first)
            assertTrue(second is Ping)
            assertEquals("Hi. Are you OK?", (second as Ping).comment)
            assertTrue((second as Ping).responseRequested?:false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val pongObject: JSONObject = JSONObject()
        pongObject.put("@id", "trust-ping_response-message-id")
        pongObject.put("@type", "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/trust_ping/1.0/ping_response")
        pongObject.put("comment", "Hi. I am OK!")
        val threadObj: JSONObject = JSONObject()
        threadObj.put("thid", "ping-id")
        pongObject.put("~thread", threadObj)
        val pong = Pong(pongObject.toString())
        try {
            val (first, second) = Message.restoreMessageInstance(pongObject.toString())
            assertTrue(first)
            assertTrue(second is Pong)
            assertEquals("Hi. I am OK!", (second as Pong).comment)
            assertEquals("ping-id", (second as Pong).getThreadId())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun test0095MsgAttaches() {
        val msg: com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message =
            com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message.builder().setContent("context")
                .setLocale("en").build()
        val att: Attach = Attach().setId("id").setMimeType("image/png").setFileName("photo.png")
            .setData("eW91ciB0ZXh0".toByteArray(java.nio.charset.StandardCharsets.UTF_8))
        msg.addAttach(att)
        assertEquals(1, msg.attaches.size)
        assertEquals(String(msg.attaches.get(0).getData()), "eW91ciB0ZXh0")
    }
}



