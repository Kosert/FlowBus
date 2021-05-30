package me.kosert.flowbus

import android.os.Looper
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Keep
class AndroidDispatcherProvider : IDispatcherProvider {

    override fun getDispatcher(): CoroutineDispatcher {
        return if (Looper.myLooper() == Looper.getMainLooper())
            Dispatchers.Main
        else
            Dispatchers.Default
    }
}