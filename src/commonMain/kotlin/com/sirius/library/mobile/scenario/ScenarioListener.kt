package com.sirius.library.mobile.scenario

import com.sirius.library.agent.listener.Event


interface ScenarioListener{
    fun  onScenarioStart(id : String,event: Event)
    fun onScenarioEnd(id : String,event: Event,success: Boolean, error: String?)
}