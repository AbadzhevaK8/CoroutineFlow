package com.example.coroutineflow.lesson14

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

suspend fun main() {
    val scope = CoroutineScope(Dispatchers.Default)

    val flow = MutableSharedFlow<Int>()

    val producer = scope.launch {
        repeat(10) {
            println("Emitted: $it")
            flow.emit(it)
            println("After emit: $it")
            delay(200)
        }
    }

    val consumer = scope.launch {
        flow.collect {
            println("Collected: $it")
            delay(1000)
        }
    }

    producer.join()
    consumer.join()
}