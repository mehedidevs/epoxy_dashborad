package com.mehedi.filter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mehedi.filter.databinding.ActivityFilterBinding
import com.mehedi.filter.sections.SingleSelectTagsModel

// Usage in Activity or Fragment
class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    private lateinit var controller: FilterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = FilterController { filterSelections ->
            // Handle filter changes and update UI or send to server
            updatePayload(filterSelections)

            Log.d("controller", "onCreate:$filterSelections ")
        }

        binding.recyclerView.setController(controller)

        // Fetch filter data from server or use mock data
        val filterResponse = fetchFilterData()
        controller.filters = filterResponse.filters
    }

    private fun updatePayload(filterSelections: Map<String, Any>) {
        val payload = FilterResponse(
            productId = "67890",
            productName = "Modern Sofa",
            filters = controller.filters.mapNotNull { filter ->  // Use `mapNotNull` to exclude null filters
                when (filter) {
                    is MultiSelectFilter -> {
                        val selectedIds =
                            filterSelections[filter.filterName] as? List<String> ?: emptyList()

                        // Only include this filter if the user has selected something
                        if (selectedIds.isNotEmpty()) {
                            filter.copy(
                                options = filter.options.filter { it.id in selectedIds }
                            )
                        } else {
                            null // Set to null if no selections were made
                        }
                    }

                    is PriceRangeFilter -> {
                        val range = filterSelections[filter.filterName] as? Pair<Int, Int>
                        if (range != null) {
                            filter.copy(minValue = range.first, maxValue = range.second)
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    is SingleSelectFilter -> {
                        val selectedId = filterSelections[filter.filterName] as? String
                        if (selectedId != null) {
                            filter.copy(
                                options = filter.options.filter { it.id == selectedId }
                            )
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    is MultiColorSelectFilter -> {
                        val selectedIds =
                            filterSelections[filter.filterName] as? List<String> ?: emptyList()
                        if (selectedIds.isNotEmpty()) {
                            filter.copy(
                                options = filter.options.filter { it.id in selectedIds }
                            )
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    is SizeRangeFilter -> {
                        val range = filterSelections[filter.filterName] as? Pair<String, String>
                        if (range != null) {
                            filter.copy(minValue = range.first, maxValue = range.second)
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    is SingleColorSelectFilter -> {
                        val selectedId = filterSelections[filter.filterName] as? String
                        if (selectedId != null) {
                            filter.copy(
                                options = filter.options.filter { it.id == selectedId }
                            )
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    is MultiSelectTagsFilter -> {
                        val selectedTags =
                            filterSelections[filter.filterName] as? List<Option> ?: emptyList()

                        if (selectedTags.isNotEmpty()) {
                            filter.copy(
                                options = filter.options.filter { it in selectedTags }, // Filter out the selected tags
                                selectedOptions = selectedTags // Preserve the selected options
                            )
                        } else {
                            null // Set to null if no selection was made
                        }
                    }

                    else -> null // For any filter that doesn't fit, just return null
                }
            }
        )

        // Log the final payload to verify the selections
        Log.d("updatePayload", "Final Payload: $payload")

        // Send the payload to the server or use it as needed
        sendPayloadToServer(payload)
    }


    private fun fetchFilterData(): FilterResponse {
        // Creating mock options for filters
        val multiSelectOptions = listOf(
            Option(id = "1", label = "Option 1"),
            Option(id = "2", label = "Option 2")
        )

        val singleSelectOptions = listOf(
            Option(id = "3", label = "Option 3"),
            Option(id = "4", label = "Option 4")
        )
        val singleTagSelectOptions = listOf(
            Option(id = "3", label = "Option 3"),
            Option(id = "4", label = "Option 4")
        )

        val multiColorSelectOptions = listOf(
            ColorOption(id = "5", label = "Red", colorCode = "#FF0000"),
            ColorOption(id = "6", label = "Blue", colorCode = "#0000FF")
        )

        // Creating mock filters
        val multiSelectFilter = MultiSelectFilter(
            filterType = "MULTI_SELECT",
            filterName = "Select Multiple",
            options = multiSelectOptions
        )

        val priceRangeFilter = PriceRangeFilter(
            filterType = "PRICE_RANGE",
            filterName = "Price Range",
            minValue = 100,
            maxValue = 500
        )

        val singleSelectFilter = SingleSelectFilter(
            filterType = "SINGLE_SELECT",
            filterName = "Select One",
            options = singleSelectOptions,
            selectedOption = null
        )

        val multiColorSelectFilter = MultiColorSelectFilter(
            filterType = "MULTI_COLOR_SELECT",
            filterName = "Select Multiple Colors",
            options = multiColorSelectOptions
        )

        val sizeRangeFilter = SizeRangeFilter(
            filterType = "SIZE_RANGE",
            filterName = "Size Range",
            minValue = "S",
            maxValue = "XL"
        )

        val singleColorSelectFilter = SingleColorSelectFilter(
            filterType = "SINGLE_COLOR_SELECT",
            filterName = "Select One Color",
            options = multiColorSelectOptions,
            selectedOption = null
        )

        val multiSelectTagsFilter = MultiSelectTagsFilter(
            filterType = "MULTI_SELECT_TAGS",
            filterName = "Select Multiple Tags",
            options = multiSelectOptions
        )
        val singleSelectTagsFilter = SingleSelectTagsFilter(
            filterType = "SINGLE_SELECT_TAGS",
            filterName = "Select Multiple Tags",
            options = singleTagSelectOptions
        )

        // Returning a mock FilterResponse object with the filters
        return FilterResponse(
            productId = "12345",
            productName = "Example Product",
            filters = listOf(
                multiSelectFilter,
                priceRangeFilter,
                singleSelectFilter,
                multiColorSelectFilter,
                sizeRangeFilter,
                singleColorSelectFilter,
                multiSelectTagsFilter,
                singleSelectTagsFilter
            )
        )
    }

    private fun sendPayloadToServer(payload: FilterResponse) {
        // Implement API call to send payload to server
        Log.d("FilterActivity", "sendPayloadToServer: $payload")
    }
}