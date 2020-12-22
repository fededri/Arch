package com.example.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {


    lateinit var viewModel: ViewModel

    private val textView by lazy { findViewById<TextView>(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModel(Updater(), CounterProcessor())
        lifecycleScope.launchWhenResumed {
            viewModel.observeState().collect {
                renderState(it)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.observeEvents().collect { event ->
                processEvent(event)
            }
        }

        findViewById<Button>(R.id.up).setOnClickListener {
            viewModel.action(Action.Up)
        }

        findViewById<Button>(R.id.down).setOnClickListener {
            viewModel.action(Action.Down)
        }
    }

    private fun processEvent(event: Event) {
        when (event) {
            is Event.LogSomething -> Log.i("Log", event.text)
        }
    }

    private fun renderState(state: State) {
        textView.text = state.counter.toString()
    }
}