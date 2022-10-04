package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

//CHALLENGE
class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //receive the tine user selects
        val timeListener = TimePickerDialog.OnTimeSetListener{
                _:TimePicker, hour: Int, min: Int ->

            val cal = Calendar.getInstance()
            cal.time = args.crimeTime
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, min)
            val timeSend = cal.time

            //package the time into BUNDLE_KEY_TIME
            //send the result through the shared key REQUEST_KEY_TIME
            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to timeSend) )
        }
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeTime
        //pass in the date to calender time because calender object will change the
        //types to ints so DatePickerFragment can handle them

        //save the minute and hour taken from the input
        //so this the initial time before the user inputs anything
        //won't change if the user not input anything
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    //to create a key to be used between fragments to communicate
    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}