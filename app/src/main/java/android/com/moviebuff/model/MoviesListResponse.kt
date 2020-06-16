package android.com.moviebuff.model


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class MoviesListResponse(
    val page: Int?,
    val results: List<Result?>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Result(
        val adult: Boolean?,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("genre_ids")
        val genreIds: List<Int?>?,
        val id: Int?,
        @SerializedName("original_language")
        val originalLanguage: String?,
        @SerializedName("original_title")
        val originalTitle: String?,
        val overview: String?,
        val popularity: Double?,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("release_date")
        val releaseDate: String?,
        val title: String?,
        val video: Boolean?,
        @SerializedName("vote_average")
        val voteAverage: Float?,
        @SerializedName("vote_count")
        val voteCount: Int?
    ) : Parcelable
}