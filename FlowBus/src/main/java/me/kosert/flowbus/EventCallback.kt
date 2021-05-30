package me.kosert.flowbus

interface EventCallback<T> {

    /**
     * This function will be called for received event
     */
    fun onEvent(event: T)
}
