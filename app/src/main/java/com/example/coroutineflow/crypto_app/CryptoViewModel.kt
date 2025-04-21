package com.example.coroutineflow.crypto_app


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    private var job: Job? = null

    fun loadData() {
        job = repository.getCurrencyList()
            .onStart { _state.value = State.Loading }
            .filter { it.isNotEmpty() }
            .onEach {
                Log.d("CryptoViewModel", "onEach: $it")
                _state.value = State.Content(currencyList = it)
            }
            .launchIn(viewModelScope)
    }

    fun stopLoading() {
        job?.cancel()
    }
}
