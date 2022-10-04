package com.bignerdranch.android.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
private const val TAG = "CrimeListViewModel"

//this ViewModel will hold the list of crimes DATA
class CrimeListViewModel : ViewModel(){
    //get access to the repository
    private val crimeRepository = CrimeRepository.get()

    //create a list of crimes
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()
    init {
        //launching COROUTINE
        //which will separate threads so if there is a suspension within this block, the UI will still be responsive to user input
        //can invoke suspending functions within it
        //ASYNCHRONOUSLY loading data
        //the data was handled in BACKGROUND THREAD \
        //pass the data into MAIN THREAD
        viewModelScope.launch {
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }
    suspend fun addCrime(crime: Crime){
        crimeRepository.addCrime(crime)
    }

    //CHALLENGE DELETE FUNCTION
    suspend fun deleteCrime(crime: Crime){
        crimeRepository.deleteCrime(crime)
    }
}