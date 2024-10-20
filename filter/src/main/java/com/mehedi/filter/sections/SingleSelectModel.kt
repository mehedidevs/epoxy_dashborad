package com.mehedi.filter.sections

import android.view.View
import android.view.ViewParent
import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.Option
import com.mehedi.filter.R
import com.mehedi.filter.SingleSelectFilter
import com.mehedi.filter.collapse
import com.mehedi.filter.databinding.ItemSingleSelectBinding
import com.mehedi.filter.expand

class SingleSelectModel(
    val filter: SingleSelectFilter,
    val onSelectionChanged: (Option) -> Unit // Change to pass `Option` instead of `String`
) : EpoxyModelWithHolder<SingleSelectModel.Holder>() {
    private var isExpanded = false
    override fun bind(holder: Holder) {
        // Set filter title
        holder.binding.tvFilterTitle.text = filter.filterName
        // Initially collapse or expand the layout based on the state
        if (!isExpanded) {
            holder.binding.rgOptions.collapse()
        } else {
            holder.binding.rgOptions.expand()
            populateOptions(holder) // Populate tag options when expanded
        }

        // Handle click event to toggle visibility with animation
        holder.binding.tvFilterTitle.setOnClickListener {
            if (isExpanded) {
                holder.binding.rgOptions.collapse()
            } else {
                holder.binding.rgOptions.expand()
                populateOptions(holder) // Populate tag options when expanding
            }
            isExpanded = !isExpanded // Toggle state
        }

    }

    private fun populateOptions(holder: Holder) {
        // Clear any previous options from the RadioGroup
        holder.binding.rgOptions.removeAllViews()

        // Populate the RadioGroup with options
        filter.options.forEach { option ->
            val radioButton = RadioButton(holder.binding.root.context).apply {
                text = option.label // Display the name of the option
                isChecked =
                    filter.selectedOption?.id == option.id // Check if this option is selected
            }

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onSelectionChanged(option) // Pass the selected `Option`
                }
            }

            holder.binding.rgOptions.addView(radioButton)
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var binding: ItemSingleSelectBinding

        override fun bindView(itemView: View) {
            binding = ItemSingleSelectBinding.bind(itemView)
        }
    }

    override fun getDefaultLayout() = R.layout.item_single_select

    override fun createNewHolder(parent: ViewParent) = Holder()
}
