package com.mehedi.filter.sections

import android.util.Log
import android.view.View
import android.view.ViewParent
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.MultiSelectFilter
import com.mehedi.filter.Option
import com.mehedi.filter.R
import com.mehedi.filter.adapters.MultiSelectAdapter
import com.mehedi.filter.databinding.ItemMultiSelectBinding



class MultiSelectModel(
    val filter: MultiSelectFilter, // The filter options
    val initiallySelectedOptions: List<Option>, // Initial selected options
    val onSelectionChanged: (List<Option>) -> Unit // Callback to notify when the selection changes
) : EpoxyModelWithHolder<MultiSelectModel.Holder>() {
    
    private var selectedOptions = initiallySelectedOptions.toMutableList() // Mutable list to track selections
    
    override fun getDefaultLayout() = R.layout.item_multi_select
    
    override fun createNewHolder(parent: ViewParent): Holder {
        return Holder()
    }
    
    override fun bind(holder: Holder) {
        // Set filter title
        holder.binding.tvFilterTitle.text = filter.filterName
        
        // Initialize the adapter for multi-select options
        val adapter = MultiSelectAdapter(
            options = filter.options,               // All available options
            selectedOptions = selectedOptions,      // Pass the tracked selected options
        ) { updatedSelectedOptions ->
            // Update the selectedOptions when the user interacts with the filter
            selectedOptions = updatedSelectedOptions.toMutableList()
            onSelectionChanged(selectedOptions) // Notify the change
            Log.d("MultiSelectModel", "bind:$selectedOptions")
        }
        
        // Set the adapter to the RecyclerView
        holder.binding.rvOptions.adapter = adapter
    }
    
    // Holder class with ViewBinding
    class Holder : EpoxyHolder() {
        lateinit var binding: ItemMultiSelectBinding
        
        override fun bindView(itemView: View) {
            binding = ItemMultiSelectBinding.bind(itemView)
        }
    }
}

