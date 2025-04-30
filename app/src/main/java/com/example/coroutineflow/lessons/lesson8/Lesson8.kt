package com.example.coroutineflow.lessons.lesson8

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend fun main() {
    val flow = getFlow()
    flow.collect {
        println("Collected: $it")
    }
    flow.collect {
        println("Collected: $it")
    }
}

fun getFlow(): Flow<Int> = flow {
    repeat(100) {
        println("Emitted: $it")
        emit(it)
        delay(1000)
    }
}