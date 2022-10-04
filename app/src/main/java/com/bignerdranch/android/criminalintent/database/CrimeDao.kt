package com.bignerdranch.android.criminalintent.database

import androidx.room.*
import com.bignerdranch.android.criminalintent.Crime
import kotlinx.coroutines.flow.Flow
import java.util.*

//@DAO lets Room to know that CrimeDao is a data access object
@Dao
interface CrimeDao {
//@Query indicates that the two functions are meant to get information from the database and not to insert ot delete
    @Query("SELECT * FROM crime")
    //asks Room to pull all the columns of all the rows of crime DB table
    //call the functions as "suspend" so Room can implement these functions as
    // suspending functions and asynchronously call them within a coroutine
    fun getCrimes(): Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    //asks Room to pull all columns whose id matches with the parameter id of the function
    suspend fun getCrime(id: UUID): Crime

    //to update an existing crime
    //make it suspend so can call it from a coroutine scope
    @Update
    suspend fun updateCrime(crime: Crime)

    //tell the Room to generate code to insert a crime into the database
    @Insert
    suspend fun addCrime(crime: Crime)

    //CHALLENGE
    //tell the Room to delete a crime from the database
    @Delete
    suspend fun deleteCrime(crime: Crime)
}