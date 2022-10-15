package com.sirius.library.agent.aries_rfc

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.utils.multibase.Base64
import com.sirius.library.utils.Date
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.StringCodec


object Utils {
    fun utcToStr(date: Date?): String {
        //    dt.strftime('%Y-%m-%dT%H:%M:%S') + '+0000'
        return ""
    }

    fun bytesToLong(bytes: ByteArray): Long {
        if (bytes.size > 8) {
            //  throw IllegalMethodParameterException("byte should not be more than 8 bytes")
        }
        var r: Long = 0
        for (i in bytes.indices) {
            r = r shl 8
            r += bytes[i]
        }
        return r
    }


    fun longToBytes(l: Long): ByteArray {
        var l = l
        val bytes = ArrayList<Byte>()
        while (l != 0L) {
            bytes.add((l % (0xff + 1)).toByte())
            l = l shr 8
        }
        val bytesp = ByteArray(bytes.size)
        var i: Int = bytes.size - 1
        var j = 0
        while (i >= 0) {
            bytesp[j] = bytes[i]
            i--
            j++
        }
        return bytesp

    }

    fun sign(
        crypto: AbstractCrypto,
        value: Any?,
        verkey: String?,
        excludeSigData: Boolean
    ): JSONObject {
        val codec = StringCodec()
        val timestampBytesFinal = ByteArray(8)

        val timestampBytes: ByteArray = longToBytes(Date().time / 1000)
        timestampBytes.copyInto(timestampBytesFinal,0,0,timestampBytes.size)
           // java.nio.ByteBuffer.allocate(8).putLong(Date().time / 1000).array()
        val sigDataBytes: ByteArray =
            timestampBytesFinal.plus(codec.fromASCIIStringToByteArray(value.toString()))
        val sigSata = codec.fromByteArrayToASCIIString(Base64.getUrlEncoder().encode(sigDataBytes))
        val signatureBytes: ByteArray? = crypto.cryptoSign(verkey, sigDataBytes)
        val signature =
            codec.fromByteArrayToASCIIString(Base64.getUrlEncoder().encode(signatureBytes))
        val data: JSONObject =
            JSONObject().put(
                "@type",
                "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/signature/1.0/ed25519Sha512_single"
            )
                .put("signer", verkey).put("signature", signature)
        if (!excludeSigData) {
            data.put("sig_data", sigSata)
        }
        return data
    }

    fun sign(crypto: AbstractCrypto, value: Any?, verkey: String?): JSONObject {
        return sign(crypto, value, verkey, false)
    }

    fun verifySigned(crypto: AbstractCrypto, signed: JSONObject): Pair<String, Boolean> {
        val codec = StringCodec()
        val signatureBytes: ByteArray = Base64.getUrlDecoder()
            .decode(codec.fromASCIIStringToByteArray(signed.optString("signature")))
        val sigDataBytes: ByteArray = Base64.getUrlDecoder()
            .decode(codec.fromASCIIStringToByteArray(signed.optString("sig_data")))
        val sigVerified: Boolean =
            crypto.cryptoVerify(signed.optString("signer"), sigDataBytes, signatureBytes)
        val dataBytes: ByteArray = Base64.getUrlDecoder().decode(signed.optString("sig_data") ?: "")
        val field = codec.fromByteArrayToASCIIString((dataBytes.copyOfRange(8, dataBytes.size)))
        return Pair(field, sigVerified)
    }
}
