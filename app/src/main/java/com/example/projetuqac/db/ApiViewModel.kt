package com.example.projetuqac.db

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetuqac.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel()
{
    private val _posts = MutableStateFlow<List<Posts>>(emptyList())
    val posts: StateFlow<List<Posts>> = _posts.asStateFlow()

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

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            _posts.value = getPosts()
//        }
//    }

//    suspend fun getPosts(): List<Posts> {
//        try {
//            // Utiliser les données de Room
//            _posts.value = apiRepository.getPosts()
//            return _posts.value
//        } catch (e: Exception) {
//            Log.e("ApiViewModel", "Error getting posts", e)
//        }
//        return emptyList()
//    }
}