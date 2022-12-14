package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope)
{

    //STORES A REFERENCE TO THE DATABASE WE CREATED
    //Room.databaseBuilder() creates a concrete implementation of the abstract CrimeDatabse
    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .build()
    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    fun updateCrime(crime: Crime){
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }
    suspend fun addCrime(crime: Crime){
        database.crimeDao().addCrime(crime)
    }
    //CHALLENGE DELETE FUNCTION
    suspend fun deleteCrime(crime: Crime){
        database.crimeDao().deleteCrime(crime)
    }
    companion object{
        private var INSTANCE: CrimeRepository? = null

        //initializes a new repository
        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }
        //accesses the repository
        fun get(): CrimeRepository{
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}