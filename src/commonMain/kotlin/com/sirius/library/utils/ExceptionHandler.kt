package com.sirius.library.utils

import com.badoo.reaktive.coroutinesinterop.asFlow
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.single.*
import kotlin.native.concurrent.ThreadLocal


interface  ExceptionListener {
    fun handleException(e : Throwable)
}
object ExceptionHandler {
 //   private val _viewState = KMediatorLiveData<ViewState>()
 //   val obser = singleUnsafe<Throwable?> {  }

    val oneTimeExceptionListener : MutableSet<ExceptionListener> = mutableSetOf()
    val noExpireExceptionListener : MutableSet<ExceptionListener> = mutableSetOf()

    fun addOneTimeListener(listener : ExceptionListener){
        oneTimeExceptionListener.add(listener)
    }

    fun removeOneTimeListener(listener : ExceptionListener){
        oneTimeExceptionListener.remove(listener)
    }

    fun clearOneTimeListener(){
        oneTimeExceptionListener.clear()
    }

    fun handleException(e : Throwable)  {
        oneTimeExceptionListener.forEach {
            it.handleException(e)
        }
        oneTimeExceptionListener.clear()
        noExpireExceptionListener.forEach {
            it.handleException(e)
        }
    }
}