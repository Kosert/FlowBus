package me.kosert.flowbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *  Automatically calls [EventsReceiver.unsubscribe] each time [lifecycleOwner] reaches STOPPED state
 */
fun EventsReceiver.bindLifecycle(lifecycleOwner: LifecycleOwner): EventsReceiver {

    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            this@bindLifecycle.unsubscribe()
        }
    })
    return this
}