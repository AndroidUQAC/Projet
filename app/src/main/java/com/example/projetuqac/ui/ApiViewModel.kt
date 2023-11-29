package com.example.projetuqac.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetuqac.db.Result
import com.example.projetuqac.db.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel()
{
    val uiState = mutableStateOf(UiState())

    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            apiRepository.getPosts().collect{result ->
                when(result) {
                    is Result.Loading -> {
                        uiState.value = UiState(isLoading = true)
                    }
                    is Result.Success -> {
                        uiState.value = UiState(posts = result.data)
                    }
                    is Result.Error -> {
                        uiState.value = UiState(posts = result.data, error = result.message)
                    }
                }
            }
        }
    }

    fun getRepository() = apiRepository

}