package me.kosert.flowbus.sample

import kotlinx.coroutines.*
import me.kosert.flowbus.GlobalBus
import kotlin.random.Random

object RandomNumberProvider {

    // Posts new random number event every 2 seconds
    fun init() {

        CoroutineScope(Job() + Dispatchers.Default).launch {
            while (true) {
                val newRandomNumber = Random.nextInt(0, 100)
                GlobalBus.post(RandomNumberEvent(newRandomNumber))
                delay(2000)
            }
        }
    }
}