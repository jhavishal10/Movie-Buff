package android.com.moviebuff.model

import android.com.moviebuff.api.RetrofitFactory
import android.com.moviebuff.core.RemoteSource
import android.com.moviebuff.core.SingletonHolder

class Repository private constructor(retrofitFactory: RetrofitFactory) {

    private val fdsApiService: ApiService =
        retrofitFactory.getApiService(ApiService::class.java) as ApiService

    suspend fun getPopularMovieList(type: String, queryMap: Map<String, Any>) =
        RemoteSource.safeApiCall { fdsApiService.getPopularMovieList(type, queryMap) }


    suspend fun getMovieDetail(id: Int, queryMap: Map<String, Any>) =
        RemoteSource.safeApiCall { fdsApiService.getMovieDetail(id, queryMap) }

    suspend fun getMovieVideo(id: Int, queryMap: Map<String, Any>) =
        RemoteSource.safeApiCall { fdsApiService.getMovieVideo(id, queryMap) }

    companion object : SingletonHolder<Repository, RetrofitFactory>(::Repository)
}
