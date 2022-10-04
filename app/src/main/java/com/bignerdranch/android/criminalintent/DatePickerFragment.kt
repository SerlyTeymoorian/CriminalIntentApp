package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

//Manage dialog you want to display
//No VIEW of its own like other fragments
class DatePickerFragment : DialogFragment() {
    //get the argument passed in to the navigation from detail list to date picker
    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //onDateListener will receive the date user selects
        val dateListener = DatePickerDialog.OnDateSetListener{
            _: DatePicker, year: Int, month: Int, day: Int ->
            //need to convert from Int to Date
            //call in GregorianCalendar object and .time to change to Date type
            val resultDate = GregorianCalendar(year, month, day).time
            //package results into key-value bundle
            //set the result into REQUEST_KEY_DATE that is shared between the two fragments
            setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
        }
        val calendar = Calendar.getInstance()
        //pass in the date to calender time because calender object will change the
        //types to ints so DatePickerFragment can handle them
        calendar.time = args.crimeDate
        //initialize the year, month, and day that the date picker should be initialized to
        //crime date passed to it
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
    //to create a key to be used between fragments to communicate
    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }
}