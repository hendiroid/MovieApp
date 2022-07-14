package com.fawry.movieapptask.business.models

data class MovieListModel(
    val page: Int = 0,
    val results: List<Item> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

data class Item(
    val adult: Boolean = false,
    val backdrop_path: String = "",
    val first_air_date: String = "",
    val genre_ids: List<Int> = listOf(),
    val id: Int = 0,
    val name: String = "",
    val origin_country: List<String> = listOf(),
    val original_language: String = "",
    val original_name: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Float = 0.0f,
    val vote_count: Int = 0

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false
        if (title != other.title) return false


        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()

        return result
    }
}