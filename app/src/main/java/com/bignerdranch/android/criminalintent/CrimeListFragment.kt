package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.*

class CrimeListFragment : Fragment() {
    //Job is a class that when launching a new coroutine, a Job instance is returned to you
    private var job: Job? = null
    private val crimeListViewModel: CrimeListViewModel by viewModels()

    //by default onCreateOptionsMenu() will not be invoked when fragment is created
    //must explicitly tell the system that fragment should receive a call to onCreateOptionsMenu()
    //call the function setHasOptionsMenu(hasMenu: Boolean)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        //make the LayoutManager Linear so the items will appear one after the other
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                //getting list of crimes from the ViewModel of the crime which holds information and data about the list of the crimes
                //LIST OF THE CRIMES
                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter =
                        CrimeListAdapter(crimes) { crimeId ->
                            findNavController().navigate(
                                //resource id of navigation action
                                CrimeListFragmentDirections.showCrimeDetail(crimeId)
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //from the menu folder -> get fragment_crime_list.xml (resource id of menu file)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.new_crime -> {
                showNewCrime()
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
    private fun showNewCrime(){
        //add an empty crime so when the user presses + a new empty crime will be created
        viewLifecycleOwner.lifecycleScope.launch{
            val newCrime = Crime (
                id = UUID.randomUUID(),
                title = " ",
                date = Date(),
                isSolved = false
            )
            //pass the empty new created crime in the addCrime
            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(newCrime.id)
            )
        }
    }
}