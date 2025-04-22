package com.example.coroutineflow.crypto_app


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    private var job: Job? = null
    private var isResumed = false

    fun loadData() {
        isResumed = true
        if (job != null) {
            return
        }
        job = repository.getCurrencyList()
            .onStart {
                Log.d("CryptoViewModel", "Started")
                _state.value = State.Loading
            }
            .filter { it.isNotEmpty() }
            .onEach {
                Log.d("CryptoViewModel", "onEach: $it")
                _state.value = State.Content(currencyList = it)
            }
            .onCompletion { Log.d("CryptoViewModel", "Complete") }
            .launchIn(viewModelScope)
    }

    fun stopLoading() {
        viewModelScope.launch {
            delay(5000)
            if (!isResumed) {
                job?.cancel()
                job = null
            } else {
                isResumed = false
            }
        }
    }
}
