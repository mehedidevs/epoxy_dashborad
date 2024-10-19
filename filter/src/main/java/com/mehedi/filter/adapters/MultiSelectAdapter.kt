package com.mehedi.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filter.Option
import com.mehedi.filter.databinding.ItemOptionBinding



class MultiSelectAdapter(
    private val options: List<Option>, // List of options
    private val selectedOptions: List<Option>, // List of currently selected options
    private val onSelectionChanged: (List<Option>) -> Unit // Callback when selection changes
) : RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>() {
    
    // Create a mutable copy of selectedOptions for internal usage
    private val selectedList = selectedOptions.toMutableList()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MultiSelectViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        val option = options[position]
        val isSelected = selectedList.any { it.id == option.id } // Compare by id
        holder.bind(option, isSelected)
        
        // Set click listener
        holder.binding.root.setOnClickListener {
            // Toggle the selection based on whether the option is already selected
            if (isSelected) {
                selectedList.removeAll { it.id == option.id } // Remove by id
            } else {
                selectedList.add(option)
            }
            // Notify the adapter to refresh the view at the current position
            notifyItemChanged(position)
            // Trigger callback to notify that selection has changed
            onSelectionChanged(selectedList)
        }
    }
    
    override fun getItemCount(): Int = options.size
    
    class MultiSelectViewHolder(val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: Option, isSelected: Boolean) {
            binding.tvOption.text = option.label // Display option label
            // Change the background based on selection state (You can customize this)
            binding.root.isSelected = isSelected
        }
    }
}

