package com.bignerdranch.android.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import java.util.*

//accepting a binding as parameter
class CrimeHolder(val binding: ListItemCrimeBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime, onCrimeClicked: (crimeID: UUID) -> Unit){
        //sets the attributes of the itemView that the CrimeHolder is referencing
        //get the title and the date of each Crime object in the list
        //set those value in corresponding TextViews
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        //setting it so when clicked on the itemView, this message will be printed
        //set it to the root view as we want to click on any row of the itemView to get the message
        binding.root.setOnClickListener{
            onCrimeClicked(crime.id)
        }
        //this is the HANDCUFF image
        //if the crime is solved then make the image visible
        binding.crimeSolved.visibility = if(crime.isSolved){
            View.VISIBLE
        } else{
            View.GONE
        }
    }
}
//parameters: a list of crimes of Type: Crime
//extending Adapter class passing the CrimeHolder to it
//                                                                                    Unit is void in JAVA
class CrimeListAdapter (private val crimes: List<Crime>, private val onCrimeClicked: (crimeID: UUID) -> Unit)
//pass the CrimeHolder which is a ViewHolder to adapter and extend it
    : RecyclerView.Adapter<CrimeHolder>()
{
    //the RecyclerView says "I need a new ViewHolder"
// ONLY CREATE A ViewHolder WITH NO DATA IN IT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        //creates binding with list_item_crime.xml and passes it to an instance of CrimeHolder
        return CrimeHolder(binding)
    }
    //PASSING THE ViewHolder INTO THE FUNCTION ALONG WITH THE POSITION
// THE ADAPTER LOOKS UP THE MODEL DATA (INPUT PARAMETER FOR THE CLASS) FOR THAT POSITION AND BINDS IT TO THE ViewModel's view
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.apply {
            holder.bind(crime, onCrimeClicked)
        }
    }

    override fun getItemCount() = crimes.size
}