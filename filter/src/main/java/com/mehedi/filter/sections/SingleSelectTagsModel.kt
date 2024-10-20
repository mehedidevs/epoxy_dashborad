package com.mehedi.filter.sections

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewParent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.Option
import com.mehedi.filter.R
import com.mehedi.filter.SingleSelectTagsFilter
import com.mehedi.filter.collapse
import com.mehedi.filter.databinding.ItemSingleSelectTagsBinding
import com.mehedi.filter.expand

class SingleSelectTagsModel(
    var filter: SingleSelectTagsFilter, // Ensure filter can be updated
    val onSelectionChanged: (Option) -> Unit
) : EpoxyModelWithHolder<SingleSelectTagsModel.Holder>() {


    private var isExpanded = false

    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

        populateOptions(holder)
    }

    private fun populateOptions(holder: Holder) {
        // Initially collapse or expand the layout based on the state
        if (!isExpanded) {
            holder.binding.flTagsOptions.collapse()
        } else {
            holder.binding.flTagsOptions.expand()
            populateTagOptions(holder) // Populate tag options when expanded
        }

        // Handle click event to toggle visibility with animation
        holder.binding.tvFilterTitle.setOnClickListener {
            if (isExpanded) {
                holder.binding.flTagsOptions.collapse()
            } else {
                holder.binding.flTagsOptions.expand()
                populateTagOptions(holder) // Populate tag options when expanding
            }
            isExpanded = !isExpanded // Toggle state
        }
    }

    private fun populateTagOptions(holder: Holder) {
        holder.binding.flTagsOptions.removeAllViews() // Clear existing views

        filter.options.forEach { tagOption ->
            val tagView = TextView(holder.binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 8, 8, 8)
                }

                text = tagOption.label // Display tag name
                setPadding(16, 8, 16, 8)
                background = ContextCompat.getDrawable(context, R.drawable.tag_background)

                // Highlight the selected tag
                if (filter.selectedOption?.id == tagOption.id) {
                    setBackgroundResource(R.drawable.selected_tag_background)
                }

                // Handle click event to change selection
                setOnClickListener {
                    // Update the selected option in the filter
                    filter = filter.copy(selectedOption = tagOption)
                    onSelectionChanged(tagOption) // Trigger callback with the selected tag
                    // Rebuild the model to reflect the new selection
                    populateTagOptions(holder) // Rebuild tag options
                }
            }

            // Add the tagView to the FlexboxLayout
            holder.binding.flTagsOptions.addView(tagView)
        }
    }


    // Epoxy holder class with ViewBinding
    class Holder : EpoxyHolder() {
        lateinit var binding: ItemSingleSelectTagsBinding

        override fun bindView(itemView: View) {
            binding = ItemSingleSelectTagsBinding.bind(itemView)
        }
    }

    override fun getDefaultLayout() = R.layout.item_single_select_tags
    override fun createNewHolder(parent: ViewParent) = Holder()
}
