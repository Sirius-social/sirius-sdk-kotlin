package com.sirius.library.utils

import com.sirius.library.utils.Base58.encode
import com.sodium.LibSodium

import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.sirius.sdk.naclJava.LibSodium;
import org.bitcoinj.core.Base58;
import org.iota.client.Client;
import org.iota.client.Message;
import org.iota.client.MessageMetadata;
import org.scijava.nativelib.NativeLoader;



object IotaUtils {
    const val MAINNET = "https://chrysalis-nodes.iota.cafe:443"
    const val TESTNET = "https://api.lb-0.h.chrysalis-devnet.iota.cafe"
    var iotaNetwork = MAINNET
    fun node(): Client {
        return Client.Builder().withNode(iotaNetwork).finish()
    }

    var msgComparator: Comparator<Message> = Comparator<Message> { o1, o2 ->
        val meta1: MessageMetadata = node().message.metadata(o1.id())
        val meta2: MessageMetadata = node().message.metadata(o2.id())
        if (meta1.milestoneIndex() < meta2.milestoneIndex()) -1 else if (meta1.milestoneIndex() > meta2.milestoneIndex()) 1 else o1.id()
            .toString().compareTo(o2.id().toString())
    }

    fun generateTag(key: ByteArray): String {
        val s = LibSodium.getInstance()
        val outputBytes = ByteArray(32)
        s.cryptoGenericHash(outputBytes, 32, key, key.size, null, 0)
        return encode(outputBytes)
    }

    init {
        try {
            NativeLoader.loadLibrary("iota_client")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
