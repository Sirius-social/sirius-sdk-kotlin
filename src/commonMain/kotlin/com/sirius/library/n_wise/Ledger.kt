package com.sirius.library.n_wise

interface Ledger {
    fun addMessage(msg: String?, tag: String?): Boolean
    fun getMessages(tag: String?): List<String?>?
}
