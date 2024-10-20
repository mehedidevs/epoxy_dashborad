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
import com.mehedi.filter.databinding.ItemSingleSelectTagsBinding

class SingleSelectTagsModel(
    var filter: SingleSelectTagsFilter, // Ensure filter can be updated
    val onSelectionChanged: (Option) -> Unit
) : EpoxyModelWithHolder<SingleSelectTagsModel.Holder>() {


    // To track the expanded/collapsed state
    private var isExpanded = false

    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

        // Set initial state as collapsed (height = 0)
        holder.binding.flTagsOptions.visibility = View.VISIBLE // Visible but with height = 0
        if (!isExpanded) {
            collapse(holder.binding.flTagsOptions, animate = false)
        }

        // Handle click event to toggle visibility with animation
        holder.binding.tvFilterTitle.setOnClickListener {
            if (isExpanded) {
                collapse(holder.binding.flTagsOptions, animate = true)
            } else {
                expand(holder.binding.flTagsOptions, animate = true)
                populateTagOptions(holder) // Populate tag options when expanded
            }
            isExpanded = !isExpanded // Toggle state
        }

        // Populate the tag options only if expanded
        if (isExpanded) {
            populateTagOptions(holder)
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
                    holder.binding.flTagsOptions.removeAllViews()
                    populateTagOptions(holder) // Rebuild tag options
                }
            }

            // Add the tagView to the FlexboxLayout
            holder.binding.flTagsOptions.addView(tagView)
        }
    }

    // Animation: Expand the view from height 0 to its measured height
    private fun expand(view: View, animate: Boolean) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = view.measuredHeight

        if (animate) {
            val animator = ValueAnimator.ofInt(0, targetHeight).apply {
                duration = 300 // Animation duration
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    view.layoutParams.height = value
                    view.requestLayout()
                }
            }
            animator.start()
        } else {
            // Set to full height without animation
            view.layoutParams.height = targetHeight
            view.requestLayout()
        }
    }

    // Animation: Collapse the view from its current height to 0
    private fun collapse(view: View, animate: Boolean) {
        val initialHeight = view.measuredHeight

        if (animate) {
            val animator = ValueAnimator.ofInt(initialHeight, 0).apply {
                duration = 300 // Animation duration
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    view.layoutParams.height = value
                    view.requestLayout()
                }
            }
            animator.start()
        } else {
            // Collapse immediately without animation
            view.layoutParams.height = 0
            view.requestLayout()
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
