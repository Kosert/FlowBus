package me.kosert.flowbus.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.kosert.flowbus.*
import me.kosert.flowbus.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val receiver = EventsReceiver()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        RandomNumberProvider.init()

        receiver.subscribe { event: RandomNumberEvent ->
            Log.i("RandomNumberEvent", "Value from EventFlow is ${event.number}")
        }

        binding.buttonTest.setOnClickListener {
            lifecycleScope.launch {
                // skip retained event and await next one
                val event = GlobalBus.getFlow<RandomNumberEvent>().drop(1).take(1).first()
                Toast.makeText(
                    this@MainActivity,
                    "Received event with number: ${event.number}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        receiver.bindLifecycle(this)
            .subscribe { event: RandomNumberEvent ->
                binding.mainTextView.text = "New random number: ${event.number}"
            }
    }
}
