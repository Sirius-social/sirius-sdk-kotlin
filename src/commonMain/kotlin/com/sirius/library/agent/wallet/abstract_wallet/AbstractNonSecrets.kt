package com.sirius.library.agent.wallet.abstract_wallet

import com.sirius.library.agent.wallet.abstract_wallet.model.RetrieveRecordOptions

abstract class AbstractNonSecrets {
    /**
     * Create a new non-secret record in the wallet
     * @param type allows to separate different record types collections
     * @param id the id of record
     * @param value  the value of record
     * @param tags the record tags used for search and storing meta information as json:
     * {
     * "tagName1": <str>, // string tag (will be stored encrypted)
     * "tagName2": <str>, // string tag (will be stored encrypted)
     * "~tagName3": <str>, // string tag (will be stored un-encrypted)
     * "~tagName4": <str>, // string tag (will be stored un-encrypted)
     * }
    </str></str></str></str> */
    abstract fun addWalletRecord(type: String?, id: String?, value: String?, tags: String?)

    /**
     * Overload method [.addWalletRecord]
     */
    fun addWalletRecord(type: String?, id: String?, value: String?) {
        addWalletRecord(type, id, value, null)
    }

    /**
     * Update a non-secret wallet record value
     * @param type allows to separate different record types collections
     * @param id  the id of record
     * @param value the value of record
     */
    abstract fun updateWalletRecordValue(type: String?, id: String?, value: String?)

    /**
     * Update a non-secret wallet record value
     * @param type allows to separate different record types collections
     * @param id  the id of record
     * @param tags tags_json: the record tags used for search and storing meta information as json:
     * {
     * "tagName1": <str>, // string tag (will be stored encrypted)
     * "tagName2": <str>, // string tag (will be stored encrypted)
     * "~tagName3": <str>, // string tag (will be stored un-encrypted)
     * "~tagName4": <str>, // string tag (will be stored un-encrypted)
     * }
    </str></str></str></str> */
    abstract fun updateWalletRecordTags(type: String?, id: String?, tags: String?)

    /**
     * Add new tags to the wallet record
     * @param type  allows to separate different record types collections
     * @param id  the id of record
     * @param tags  tags_json: the record tags used for search and storing meta information as json:
     * {
     * "tagName1": <str>, // string tag (will be stored encrypted)
     * "tagName2": <str>, // string tag (will be stored encrypted)
     * "~tagName3": <str>, // string tag (will be stored un-encrypted)
     * "~tagName4": <str>, // string tag (will be stored un-encrypted)
     * }
    </str></str></str></str> */
    abstract fun addWalletRecordTags(type: String?, id: String?, tags: String?)

    /**
     * Delete tags from the wallet record
     * @param type allows to separate different record types collections
     * @param id the id of record
     * @param tagNames the list of tag names to remove from the record as json array: ["tagName1", "tagName2", ...]
     */
    abstract fun deleteWalletRecord(type: String?, id: String?, tagNames: List<String?>?)

    /**
     * Delete an existing wallet record in the wallet
     * @param type allows to separate different record types collections
     * @param id the id of record
     */
    abstract fun deleteWalletRecord(type: String?, id: String?)

    /**
     * Get an wallet record by id
     * @param type  allows to separate different record types collections
     * @param id  the id of record
     * @param options  {
     * retrieveType: (optional, false by default) Retrieve record type,
     * retrieveValue: (optional, true by default) Retrieve record value,
     * retrieveTags: (optional, true by default) Retrieve record tags
     * }
     * @return wallet record json:
     * {
     * id: "Some id",
     * type: "Some type", // present only if retrieveType set to true
     * value: "Some value", // present only if retrieveValue set to true
     * tags: <tags json>, // present only if retrieveTags set to true
     * }
    </tags> */
    abstract fun getWalletRecord(type: String?, id: String?, options: RetrieveRecordOptions?): String?

    /**
     * Search for wallet records
     * @param type allows to separate different record types collections
     * @param query MongoDB style query to wallet record tags:
     * {
     * "tagName": "tagValue",
     * $or: {
     * "tagName2": { $regex: 'pattern' },
     * "tagName3": { $gte: '123' },
     * },
     * }
     * @param options {
     * retrieveRecords: (optional, true by default) If false only "counts" will be calculated,
     * retrieveTotalCount: (optional, false by default) Calculate total count,
     * retrieveType: (optional, false by default) Retrieve record type,
     * retrieveValue: (optional, true by default) Retrieve record value,
     * retrieveTags: (optional, true by default) Retrieve record tags,
     * }
     * @param limit max record count to retrieve
     * @return wallet records json:
     * {
     * totalCount: <str>, // present only if retrieveTotalCount set to true
     * records: [{ // present only if retrieveRecords set to true
     * id: "Some id",
     * type: "Some type", // present only if retrieveType set to true
     * value: "Some value", // present only if retrieveValue set to true
     * tags: <tags json>, // present only if retrieveTags set to true
     * }],
     * }
    </tags></str> */
    abstract fun walletSearch(
        type: String?,
        query: String?,
        options: RetrieveRecordOptions?,
        limit: Int
    ): Pair<List<String>, Int>

    /**
     * Overload method [.walletSearch]
     */
    fun walletSearch(type: String?, query: String?, options: RetrieveRecordOptions?): Pair<List<String>, Int> {
        return walletSearch(type, query, options, 1)
    }
}
