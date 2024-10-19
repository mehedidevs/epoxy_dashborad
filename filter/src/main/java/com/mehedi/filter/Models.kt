package com.mehedi.filter

// Data classes
data class FilterResponse(
    val productId: String,
    val productName: String,
    val filters: List<Filter>
)

sealed class Filter {
    abstract val filterType: String
    abstract val filterName: String
}

data class MultiSelectFilter(
    override val filterType: String,
    override val filterName: String,
    val options: List<Option>
) : Filter()

data class PriceRangeFilter(
    override val filterType: String,
    override val filterName: String,
    val minValue: Int,
    val maxValue: Int
) : Filter()

data class SingleSelectFilter(
    override val filterType: String,
    override val filterName: String,
    val options: List<Option>
) : Filter()

data class MultiColorSelectFilter(
    override val filterType: String,
    override val filterName: String,
    val options: List<ColorOption>
) : Filter()

data class SizeRangeFilter(
    override val filterType: String,
    override val filterName: String,
    val minValue: String,
    val maxValue: String
) : Filter()

data class SingleColorSelectFilter(
    override val filterType: String,
    override val filterName: String,
    val options: List<ColorOption>
) : Filter()

data class MultiSelectTagsFilter(
    override val filterType: String,
    override val filterName: String,
    val options: List<Option>
) : Filter()

data class Option(val id: String, val label: String)
data class ColorOption(val id: String, val label: String, val colorCode: String)





