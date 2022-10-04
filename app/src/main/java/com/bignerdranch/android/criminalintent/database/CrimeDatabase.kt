package com.bignerdranch.android.criminalintent.database

import androidx.room.*
import com.bignerdranch.android.criminalintent.Crime
import java.sql.Time

//ROOM WILL CREATE AND MANAGE TABLES FOR THIS DATABASE
//@Database annotation tells Room that this class represents a DATABASE
//PARAMETERS:
//  1st: a list of entity classes which tells Room which entity classes to use when creating and managing tables for this database
//  2nd: version of the Database. When first creating it, make the version 1

@Database(
    entities = [ Crime::class ],
    version = 3,
    exportSchema = true, autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)]
)
//explicitly add the CONVERTERS to the DATABASE
@TypeConverters(CrimeTypeConverters::class)

//abstract so no instance of it can be made
abstract class CrimeDatabase :RoomDatabase() {
    //when the DB is created, Room will generate a concrete implementation of the DAO that you can access
    abstract fun crimeDao(): CrimeDao
}


