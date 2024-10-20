package com.mehedi.filter

import com.airbnb.epoxy.EpoxyController
import com.mehedi.filter.sections.MultiColorSelectModel
import com.mehedi.filter.sections.MultiSelectModel
import com.mehedi.filter.sections.MultiSelectTagsModel
import com.mehedi.filter.sections.SingleColorSelectModel
import com.mehedi.filter.sections.SingleSelectModel
import com.mehedi.filter.sections.SingleSelectTagsModel

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

                is MultiColorSelectFilter -> {
                    val selectedColorOptions =
                        filterSelections[filter.filterName] as? List<ColorOption> ?: emptyList()

                    MultiColorSelectModel(
                        filter = filter.copy(selectedOptions = selectedColorOptions),
                        onSelectionChanged = { selectedColors ->
                            // Store the selected color options
                            filterSelections[filter.filterName] = selectedColors
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }

                is MultiSelectTagsFilter -> {
                    val selectedTagOptions =
                        filterSelections[filter.filterName] as? List<Option> ?: emptyList()

                    MultiSelectTagsModel(
                        filter = filter.copy(selectedOptions = selectedTagOptions), // Pass the selected tags
                        onSelectionChanged = { selectedTags ->
                            // Store the selected tags
                            filterSelections[filter.filterName] = selectedTags
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }

                is SingleSelectTagsFilter -> {
                    val selectedTag = filterSelections[filter.filterName] as? Option

                    SingleSelectTagsModel(
                        filter = filter.copy(selectedOption = selectedTag), // Pass the selected tag
                        onSelectionChanged = { selectedTagOption ->
                            // Store the selected tag option
                            filterSelections[filter.filterName] = selectedTagOption
                            onFilterChanged(filterSelections)
                        }
                    ).id(filter.filterName).addTo(this)
                }


                else -> {}
            }
        }
    }
}

