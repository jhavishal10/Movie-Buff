package android.com.moviebuff.ui.detail

import android.com.moviebuff.R
import androidx.annotation.LayoutRes

sealed class MovieDetailItem(@LayoutRes val viewType: Int) {
    data class Information(
        val poster: String,
        val rating: Float,
        val releaseDate: String,
        val adult: Boolean,
        val budget: String,
        val status: String,
        val revenue: String,
        val overviewText: String
    ) : MovieDetailItem(
        R.layout.movie_detail_information
    )
}