package com.sirius.library.mobile.scenario

import com.sirius.library.messaging.Message


interface EventStorageAbstract {
     fun eventStore(id : String, event: Pair<String?, Message?>?, fields : Map<String,Any?>? )
     fun eventRemove(id : String)
     fun getEvent(id : String) : Pair<String?, Message>?
}