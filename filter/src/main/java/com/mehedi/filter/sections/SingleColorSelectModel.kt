package com.mehedi.filter.sections

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.ColorOption
import com.mehedi.filter.R
import com.mehedi.filter.SingleColorSelectFilter
import com.mehedi.filter.collapse
import com.mehedi.filter.databinding.ItemSingleColorSelectBinding
import com.mehedi.filter.expand


class SingleColorSelectModel(
    var filter: SingleColorSelectFilter,
    val onSelectionChanged: (ColorOption) -> Unit
) : EpoxyModelWithHolder<SingleColorSelectModel.Holder>() {
    private var isExpanded = false
    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

        // Initially collapse or expand the layout based on the state
        if (!isExpanded) {
            holder.binding.flColorOptions.collapse()
        } else {
            holder.binding.flColorOptions.expand()
            populateOptions(holder) // Populate tag options when expanded
        }

        // Handle click event to toggle visibility with animation
        holder.binding.tvFilterTitle.setOnClickListener {
            if (isExpanded) {
                holder.binding.flColorOptions.collapse()
            } else {
                holder.binding.flColorOptions.expand()
                populateOptions(holder) // Populate tag options when expanding
            }
            isExpanded = !isExpanded // Toggle state
        }


    }

    private fun populateOptions(holder: Holder) {
        // Clear any previous views from the FlexboxLayout
        holder.binding.flColorOptions.removeAllViews()

        // Populate the FlexboxLayout with circular color options
        filter.options.forEach { colorOption ->
            // Create a View to represent the color
            val colorView = FrameLayout(holder.binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(60, 60).apply {
                    setMargins(8, 8, 8, 8)
                }

                // Create a GradientDrawable for the circular color option
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(Color.parseColor(colorOption.colorCode)) // Set the color
                }

                // Set the drawable as the background of the colorView
                background = drawable

                // If the color option is selected, add a checkmark icon
                if (filter.selectedOption?.id == colorOption.id) {
                    val checkIcon = ImageView(context).apply {
                        layoutParams = FrameLayout.LayoutParams(40, 40).apply {
                            gravity = Gravity.CENTER
                        }
                        setImageResource(R.drawable.round_check_24) // Set your check icon resource here
                        setColorFilter(Color.WHITE) // Optionally tint the checkmark
                    }
                    addView(checkIcon)
                }

                // Handle click event to change selection
                setOnClickListener {
                    // Update the selected option in the filter
                    filter = filter.copy(selectedOption = colorOption)
                    onSelectionChanged(colorOption) // Trigger callback
                    // Rebuild the model to reflect the new selection
                    holder.binding.flColorOptions.removeAllViews()
                    bind(holder) // Rebind the holder to refresh the UI
                }
            }

            // Add the colorView to the FlexboxLayout
            holder.binding.flColorOptions.addView(colorView)
        }
    }


    /*  override fun bind(holder: Holder) {
          // Set the filter title
          holder.binding.tvFilterTitle.text = filter.filterName

          // Clear any previous views from the FlexboxLayout
          holder.binding.flColorOptions.removeAllViews()

          // Populate the FlexboxLayout with circular color options
          filter.options.forEach { colorOption ->
              val colorView = View(holder.binding.root.context).apply {
                  layoutParams = LinearLayout.LayoutParams(80, 80).apply {
                      setMargins(8, 8, 8, 8)
                  }

                  // Create a GradientDrawable for the circular color option
                  val drawable = GradientDrawable().apply {
                      shape = GradientDrawable.OVAL
                      setColor(Color.parseColor(colorOption.colorCode)) // Set the color

                      // Add stroke for the selected color
                      if (filter.selectedOption?.id == colorOption.id) {
                          setStroke(6, Color.BLACK) // Set a red border if selected
                      }
                  }

                  // Set the drawable as the background of the colorView
                  background = drawable

                  // Handle click event to change selection
                  setOnClickListener {
                      // Update the selected option in the filter
                      filter = filter.copy(selectedOption = colorOption)
                      onSelectionChanged(colorOption) // Trigger callback
                      // Rebuild the model to reflect the new selection
                      holder.binding.flColorOptions.removeAllViews()
                      bind(holder) // Rebind the holder to refresh the UI
                  }
              }

              // Add the colorView to the FlexboxLayout
              holder.binding.flColorOptions.addView(colorView)
          }
      }*/


    // Holder class with ViewBinding
    class Holder : EpoxyHolder() {
        lateinit var binding: ItemSingleColorSelectBinding

        override fun bindView(itemView: View) {
            binding = ItemSingleColorSelectBinding.bind(itemView)
        }
    }

    override fun getDefaultLayout() = R.layout.item_single_color_select
    override fun createNewHolder(parent: ViewParent) = Holder()
}
