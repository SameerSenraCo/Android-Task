package com.senra.acharyaprashantorgtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senra.acharyaprashantorgtask.model.MediaCoverage
import com.senra.acharyaprashantorgtask.repo.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val repository: MediaRepository) : ViewModel() {

    private val _mediaCoverages = MutableLiveData<List<MediaCoverage>>()
    val mediaCoverages: LiveData<List<MediaCoverage>> get() = _mediaCoverages

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        fetchMediaCoverages(100) // Fetch initial media coverages
    }

    private fun fetchMediaCoverages(limit: Int) {
        viewModelScope.launch {
            val result = repository.fetchMediaCoverages(limit)
            if (result.isSuccess) {
                _mediaCoverages.value = result.getOrNull() ?: emptyList() // Update live data on success
                _errorMessage.value = null // Clear error message
            } else if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error occurred" // Set error message
            }
        }
    }
}
