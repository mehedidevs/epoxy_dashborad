package com.mehedi.filter

import com.airbnb.epoxy.EpoxyController
import com.mehedi.filter.sections.MultiSelectModel
import com.mehedi.filter.sections.SingleColorSelectModel
import com.mehedi.filter.sections.SingleSelectModel

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
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }

                // Single-select filter handling
                is SingleSelectFilter -> {
                    val selectedOptionId = filterSelections[filter.filterName] as? String

                    SingleSelectModel(
                        filter = filter,
                        onSelectionChanged = { selectedOption ->
                            // Store the selected option ID
                            filterSelections[filter.filterName] = selectedOption.id
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }

                is SingleColorSelectFilter -> {
                    val selectedColorOptionId = filterSelections[filter.filterName] as? String

                    SingleColorSelectModel(
                        filter = filter.copy(selectedOption = filter.options.find { it.id == selectedColorOptionId }),
                        onSelectionChanged = { selectedColorOption ->
                            // Store the selected color option's ID
                            filterSelections[filter.filterName] = selectedColorOption.id
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }


              /*  // Single-select filter handling with selectedOptionId
                is SingleSelectFilter -> {
                    val selectedOptionId = filterSelections[filter.filterName] as? String

                    SingleSelectModel(
                        filter = filter.copy(selectedOption = filter.options.find { it.id == selectedOptionId }), // Pre-select the option if available
                        onSelectionChanged = { selectedOption ->
                            // Store the selected option ID
                            filterSelections[filter.filterName] = selectedOption.id
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }*/


                else -> {}
            }
        }
    }
}

