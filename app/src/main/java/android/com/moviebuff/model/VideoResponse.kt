package android.com.moviebuff.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class VideoResponse(
    val id: Int?,
    val results: List<Result?>?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Result(
        val id: String?,
        @SerializedName("iso_3166_1")
        val iso31661: String?,
        @SerializedName("iso_639_1")
        val iso6391: String?,
        val key: String?,
        val name: String?,
        val site: String?,
        val size: Int?,
        val type: String?
    ) : Parcelable
}