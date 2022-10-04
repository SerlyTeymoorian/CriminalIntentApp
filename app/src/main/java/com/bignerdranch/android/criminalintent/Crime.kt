package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalTime
import java.util.*

//the constructor of Crime class
//add the properties to it
//the Crime class represents a single office crime

//to create a table for the crimes
@Entity
data class Crime(
    //set the ID to be the primary key because it is unique
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
)
