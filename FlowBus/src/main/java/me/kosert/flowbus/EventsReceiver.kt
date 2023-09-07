package me.kosert.flowbus

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Class for receiving events posted to [FlowBus]
 *
 *@param bus [FlowBus] instance to subscribe to. If not set, [GlobalBus] will be used
 */
open class EventsReceiver @JvmOverloads constructor(
    private val bus: FlowBus = GlobalBus
) {

    private val jobs = mutableMapOf<Class<*>, Job>()

    private var returnDispatcher: CoroutineDispatcher = DispatcherProvider.get()

    /**
     * Set the `CoroutineDispatcher` which will be used to launch your callbacks.
     *
     * If this [EventsReceiver] was created on the main thread the default dispatcher will be [Dispatchers.Main].
     * In any other case [Dispatchers.Default] will be used.
     */
    fun returnOn(dispatcher: CoroutineDispatcher): EventsReceiver {
        returnDispatcher = dispatcher
        return this
    }

    /**
     * Subscribe to events that are type of [clazz] with the given [callback] function.
     * The [callback] can be called immediately if event of type [clazz] is present in the flow.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback The callback function
     * @return This instance of [EventsReceiver] for chaining
     */
    @JvmOverloads
    fun <T : Any> subscribeTo(
        clazz: Class<T>,
        skipRetained: Boolean = false,
        callback: suspend (event: T) -> Unit
    ): EventsReceiver {

        if (jobs.containsKey(clazz)) return this

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throw throwable
        }

        val job = CoroutineScope(Job() + Dispatchers.Default + exceptionHandler).launch {
            bus.forEvent(clazz)
                .drop(if (skipRetained) 1 else 0)
                .filterNotNull()
                .collect { withContext(returnDispatcher) { callback(it) } }
        }

        jobs[clazz] = job
        return this
    }

    /**
     * A variant of [subscribeTo] that uses an instance of [EventCallback] as callback.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback Interface with implemented callback function
     * @return This instance of [EventsReceiver] for chaining
     * @see [subscribeTo]
     */
    @JvmOverloads
    fun <T : Any> subscribeTo(
        clazz: Class<T>,
        callback: EventCallback<T>,
        skipRetained: Boolean = false
    ): EventsReceiver = subscribeTo(clazz, skipRetained) { callback.onEvent(it) }

    /**
     * Unsubscribe from events type of [clazz]
     */
    fun <T : Any> unsubscribe(clazz: Class<T>) {
        jobs.remove(clazz)?.cancel()
    }

    /**
     * Unsubscribe from all events
     */
    fun unsubscribe() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
