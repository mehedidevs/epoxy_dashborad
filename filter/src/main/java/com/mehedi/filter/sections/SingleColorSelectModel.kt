package com.mehedi.filter.sections

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewParent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mehedi.filter.ColorOption
import com.mehedi.filter.R
import com.mehedi.filter.SingleColorSelectFilter
import com.mehedi.filter.databinding.ItemSingleColorSelectBinding


class SingleColorSelectModel(
    val filter: SingleColorSelectFilter,
    val onSelectionChanged: (ColorOption) -> Unit
) : EpoxyModelWithHolder<SingleColorSelectModel.Holder>() {

    override fun bind(holder: Holder) {
        // Set the filter title
        holder.binding.tvFilterTitle.text = filter.filterName

        // Clear any previous views from the FlexboxLayout
        holder.binding.flColorOptions.removeAllViews()

        // Populate the FlexboxLayout with circular color options
        filter.options.forEach { colorOption ->
            val colorView = View(holder.binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                    setMargins(8, 8, 8, 8)
                }

                // Set the background color programmatically
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(Color.parseColor(colorOption.colorCode)) // Set color based on option

                    // Add a border if this is the selected option
                    if (filter.selectedOption?.id == colorOption.id) {
                        setStroke(6, Color.RED) // Set the border color and width
                    }
                }

                background = drawable

                // Handle click event to change selection
                setOnClickListener {
                    onSelectionChanged(colorOption)
                    // Set the background color programmatically
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        setColor(Color.parseColor(colorOption.colorCode)) // Set color based on option

                        // Add a border if this is the selected option
                        if (filter.selectedOption?.id == colorOption.id) {
                            setStroke(6, Color.RED) // Set the border color and width
                        }
                    }
                }
            }

            // Add the colorView to the FlexboxLayout
            holder.binding.flColorOptions.addView(colorView)
        }
    }


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
