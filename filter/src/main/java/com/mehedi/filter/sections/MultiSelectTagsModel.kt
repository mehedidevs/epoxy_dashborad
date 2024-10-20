package com.mehedi.filter.sections

import android.view.View
import android.view.ViewParent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.MultiSelectTagsFilter
import com.mehedi.filter.Option
import com.mehedi.filter.R
import com.mehedi.filter.databinding.ItemMultiSelectTagsBinding


class MultiSelectTagsModel(
    var filter: MultiSelectTagsFilter, // Ensure the filter can be updated
    val onSelectionChanged: (List<Option>) -> Unit
) : EpoxyModelWithHolder<MultiSelectTagsModel.Holder>() {

    // This list will keep track of the selected tags
    private val selectedTags = mutableListOf<Option>()

    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

        // Clear any previous views from the FlexboxLayout
        holder.binding.flTagsOptions.removeAllViews()

        // Populate the FlexboxLayout with tags
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

                // Highlight the selected tags
                if (selectedTags.contains(tagOption)) {
                    setBackgroundResource(R.drawable.selected_tag_background)
                }

                // Handle click event to change selection
                setOnClickListener {
                    // Toggle the selection: add or remove from the list
                    if (selectedTags.contains(tagOption)) {
                        selectedTags.remove(tagOption)
                    } else {
                        selectedTags.add(tagOption)
                    }
                    onSelectionChanged(selectedTags) // Trigger callback with the new list
                    // Rebuild the model to reflect the new selection
                    holder.binding.flTagsOptions.removeAllViews()
                    bind(holder) // Rebind the holder to refresh the UI
                }
            }

            // Add the tagView to the FlexboxLayout
            holder.binding.flTagsOptions.addView(tagView)
        }
    }

    // Epoxy holder class with ViewBinding
    class Holder : EpoxyHolder() {
        lateinit var binding: ItemMultiSelectTagsBinding

        override fun bindView(itemView: View) {
            binding = ItemMultiSelectTagsBinding.bind(itemView)
        }
    }

    override fun getDefaultLayout() = R.layout.item_multi_select_tags
    override fun createNewHolder(parent: ViewParent) = Holder()
}
