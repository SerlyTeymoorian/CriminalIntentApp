package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.LocalTime
import java.util.*

private const val TAG = "CrimeDetailFragment"

//turns into fragment by inheriting Fragment()
class CrimeDetailFragment : Fragment(){
    private var _binding: FragmentCrimeDetailBinding?= null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val args: CrimeDetailFragmentArgs by navArgs()

    //ADDED
    private val crimeRepository = CrimeRepository.get()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeID)
    }

    //ADDED
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    //return the binding root
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflates and binds fragment_crime_detail.xml
        //binding is initially null here and then we create it as it was made null in onDestroy()
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //still using binding and not _binding as binding is not null
        binding.apply {
            // EditText by its id crime_title
            //whatever the user types will be saved in text
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
                //saving the title of the crime into the variable crime of Crime class
                // crime = crime.copy(title = text.toString())
            }
            //CHECKBOX
            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
                // crime = crime.copy(isSolved = isChecked)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }

        //the request key will be shared between the two fragments
        //the lambda expression that will be invoked when CrimeDetailFragment is in the STARTED
        //lifecycle state with a result to consume
        //FragmentManager keeps track of the listener
        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            //the lambda expression is invoked only when the user tries to make changes and save
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime {  it.copy(date = newDate) }
        }

        //CHALLENGE TIME
        setFragmentResultListener(
            TimePickerFragment.REQUEST_KEY_TIME
        ) {_, bundle ->
            val newTime =
                bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            crimeDetailViewModel.updateCrime { it.copy(date = newTime) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding allows to null out in destroy so no reference to the fragment is held while not viewing the fragment itself
        _binding = null
    }
    //have access to the latest crime
    private fun updateUi(crime: Crime){
        binding.apply {
            //check whether the existing title matches to the input title
            if(crimeTitle.text.toString() != crime.title){
                crimeTitle.setText(crime.title)
            }
            //DATE
            crimeDate.text = crime.date.toString()

            //WHEN CLICKING ON THE DATE IN THE CrimeDetailList, THEN IT WILL NAVIGATE TO DatePickerFragment
            crimeDate.setOnClickListener {
                findNavController().navigate(
                    //THE ID OF THE ACTION FROM DetailList to DatePicker is select_date
                    //pass in the current date of the crime (crime.date) as an argument because
                    //the DatePicker fragment navigation requires argument when navigating to it
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }
            //CHALLENGE for TIME
            crimeTime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(crime.date)
                )
            }
            crimeSolved.isChecked = crime.isSolved
        }
    }
    //ADDED
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //from the menu folder -> get fragment_crime_list.xml (resource id of menu file)
        inflater.inflate(R.menu.fragment_crime_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_crime -> {
                //call deleteCrime and pass in the crimeId to delete from
                deleteCrime(args.crimeID)
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
    private fun deleteCrime(crimeId: UUID)
    {
        viewLifecycleOwner.lifecycleScope.launch{
            val crime = crimeRepository.getCrime(crimeId)
            crimeListViewModel.deleteCrime(crime)
            findNavController().navigate(
                //go back to the list after deleting
                CrimeDetailFragmentDirections.selectDelete()
            )
        }
    }
}