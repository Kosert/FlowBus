package me.kosert.flowbus

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface IDispatcherProvider {
    fun getDispatcher(): CoroutineDispatcher
}

internal class DefaultDispatcherProvider: IDispatcherProvider {
    override fun getDispatcher() = Dispatchers.Default
}

/**
 * This class is responsible for checking whether any environmental specific [IDispatcherProvider] is also implemented in project (currently there is only one in `FlowBus-android`).
 * If so, the [IDispatcherProvider] implementations can decide what Dispatcher should [EventsReceiver] use by default.
 */
internal object DispatcherProvider {

    private const val ANDROID_CLASS = "me.kosert.flowbus.AndroidDispatcherProvider"

    private val provider by lazy {
        val providerNames = listOf<String>(ANDROID_CLASS)

        providerNames.mapNotNull { forName(it) }
            .firstOrNull() as IDispatcherProvider?
            ?: DefaultDispatcherProvider()
    }

    private fun forName(name: String) = runCatching {
        Class.forName(name)
    }.getOrNull()?.getDeclaredConstructor()?.newInstance()

    fun get() = provider.getDispatcher()
}