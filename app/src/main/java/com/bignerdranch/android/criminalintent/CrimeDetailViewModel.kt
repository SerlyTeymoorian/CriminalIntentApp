package com.bignerdranch.android.criminalintent

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

//fragments are recreated during configuration changes
//StateFlow holds a Crime => to provide consumers with the freshest Data
//as StateFlow is updated => changes pushed out to the CrimeDetailFragment
class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    //expose the detail screen as StateFlow
    //val instead of var so the crimes can only be read and not be written
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }
    fun updateCrime(onUpdate: (Crime) -> Crime){
        _crime.update { oldCrime ->
            oldCrime?.let{ onUpdate(it) }
        }
    }

    //when navigate away from a fragment, onCleared() of ViewModel is called
    //and loses the data saved in that ViewModel
    //Perfect place in onCleared() to save the changes to the crime
    //so it will be used in DetailFragment
    override fun onCleared() {
        super.onCleared()

        crime.value?.let { crimeRepository.updateCrime(it) }
    }
}

//create this class so you can pass in crime id to the CONSTRUCTOR OF CrimeDetailViewModel
class CrimeDetailViewModelFactory(private val crimeId: UUID) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}