package com.mehedi.filter

import android.util.Log
import com.airbnb.epoxy.EpoxyController
import com.mehedi.filter.sections.MultiSelectModel

class FilterController(private val onFilterChanged: (Map<String, Any>) -> Unit) :
    EpoxyController() {
    var filters: List<Filter> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }
    
    private val filterSelections = mutableMapOf<String, Any>()
    
    override fun buildModels() {
        filters.forEach { filter ->
            when (filter) {
                is MultiSelectFilter -> {
                    val selectedOptions =
                        filterSelections[filter.filterName] as? List<String> ?: emptyList()
                    
                    MultiSelectModel(
                        filter = filter,
                        initiallySelectedOptions = filter.options.filter { it.id in selectedOptions },
                        onSelectionChanged = { selections ->
                            // Map the selected options by ID
                            val selectedIds = selections.map { it.id }
                            filterSelections[filter.filterName] = selectedIds
                            Log.d(
                                "FilterController",
                                "Selections for ${filter.filterName}: $selectedIds"
                            )
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }
                
                
                else -> {}
            }
        }
    }
}

