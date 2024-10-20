package com.mehedi.filter.sections

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.ColorOption
import com.mehedi.filter.MultiColorSelectFilter
import com.mehedi.filter.R
import com.mehedi.filter.SingleColorSelectFilter
import com.mehedi.filter.databinding.ItemMultiColorSelectBinding
import com.mehedi.filter.databinding.ItemSingleColorSelectBinding


class MultiColorSelectModel(
    var filter: MultiColorSelectFilter,
    val onSelectionChanged: (List<ColorOption>) -> Unit
) : EpoxyModelWithHolder<MultiColorSelectModel.Holder>() {

    // This list will keep track of the selected colors
    private val selectedColors = mutableListOf<ColorOption>()

    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

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
                if (selectedColors.contains(colorOption)) {
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
                    // Toggle the selection: add or remove from the list
                    if (selectedColors.contains(colorOption)) {
                        selectedColors.remove(colorOption)
                    } else {
                        selectedColors.add(colorOption)
                    }
                    onSelectionChanged(selectedColors) // Trigger callback with the new list
                    // Rebuild the model to reflect the new selection
                    holder.binding.flColorOptions.removeAllViews()
                    bind(holder) // Rebind the holder to refresh the UI
                }
            }

            // Add the colorView to the FlexboxLayout
            holder.binding.flColorOptions.addView(colorView)
        }
    }


    // Holder class with ViewBinding
    class Holder : EpoxyHolder() {
        lateinit var binding: ItemMultiColorSelectBinding

        override fun bindView(itemView: View) {
            binding = ItemMultiColorSelectBinding.bind(itemView)
        }
    }

    override fun getDefaultLayout() = R.layout.item_multi_color_select
    override fun createNewHolder(parent: ViewParent) = Holder()
}
