package android.com.moviebuff.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class MovieDetailResponse(
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val budget: Long?,
    val id: Long?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val revenue: Long?,
    val status: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?
) : Parcelable