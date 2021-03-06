package android.com.moviebuff.model


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

@JvmSuppressWildcards
interface ApiService {

    @GET("/3/movie/{type}")
    suspend fun getPopularMovieList(
        @Path("type") type: String,
        @QueryMap queryMap: Map<String, Any>
    ): Response<MoviesListResponse>

    @GET("/3/movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @QueryMap queryMap: Map<String, Any>
    ): Response<MovieDetailResponse>

    @GET("/3/movie/{id}/videos")
    suspend fun getMovieVideo(
        @Path("id") id: Int,
        @QueryMap queryMap: Map<String, Any>
    ): Response<VideoResponse>
}