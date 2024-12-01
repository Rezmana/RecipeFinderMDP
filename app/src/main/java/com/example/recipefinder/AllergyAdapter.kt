package com.example.recipefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

//class AllergyAdapter(
//    private val allergyList: List<String>, // The list of allergies to display
//    private val onAllergyCheckedChanged: MutableList<String> // Callback to handle changes
//) : RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder>() {
//
//    private val selectedAllergies = mutableSetOf<AllergyModel>() // Keep track of selected items
//
//    // ViewHolder class to bind allergy data
//    inner class AllergyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val checkBox: CheckBox = itemView.findViewById(R.id.allergyCheckbox)
//
//        fun bind(allergy: AllergyModel) {
//            checkBox.text = allergy.name
//            checkBox.isChecked = allergy.isSelected
//
//            // Handle checkbox state changes
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                allergy.isSelected = isChecked
//                if (isChecked) {
//                    selectedAllergies.add(allergy)
//                } else {
//                    selectedAllergies.remove(allergy)
//                }
//                // Notify the fragment about the updated list
//                onAllergyCheckedChanged(selectedAllergies.toList())
//            }
//        }
//    }
//
//    // Create ViewHolder
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.allergy_view, parent, false)
//        return AllergyViewHolder(view)
//    }
//
//    // Bind ViewHolder
//    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
//        holder.bind(allergyList[position])
//    }
//
//    // Return item count
//    override fun getItemCount(): Int = allergyList.size
//
//    // Clear all selections
//    fun clearSelections() {
//        allergyList.forEach { it.isSelected = false }
//        selectedAllergies.clear()
//        notifyDataSetChanged()
//    }
//}
class AllergyAdapter(
    private val allergies: List<String>,
    private val selectedAllergies: MutableList<String>
) : RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder>() {

    inner class AllergyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.allergyCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.allergy_view, parent, false)
        return AllergyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
        val allergy = allergies[position]
        holder.checkBox.text = allergy
        holder.checkBox.isChecked = selectedAllergies.contains(allergy)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add(allergy)
            } else {
                selectedAllergies.remove(allergy)
            }
        }
    }

    override fun getItemCount(): Int = allergies.size
}


