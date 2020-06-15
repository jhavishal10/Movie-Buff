package android.com.moviebuff.model


import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    @GET("/3/discover/movie")
    suspend fun getApiRepoList(@QueryMap queryMap: Map<String, Any>): Response<ApiRepoResponse>
}