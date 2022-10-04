package com.bignerdranch.android.criminalintent

//MUST REGISTER IT IN AndroidManifest.xml
//SO THE OS WILL CREATE AN INSTANCE OF CriminalIntentApplication when launching the app
//THEN OS WILL CALL onCreate() ON THAT INSTANCE WHICH WILL INITTIALIZE Crimerepository
import android.app.Application

//create this class that extends from Application
//override onCreate so when the app is starting (first loaded into memory), it will initialize a CrimeRepository
//as long as the app is in the memory, the repository will exist
//CriminalIntentApplication will not be recreated on every configuration change because
// only created when app is created and destroyed when the app is not in memory
class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}