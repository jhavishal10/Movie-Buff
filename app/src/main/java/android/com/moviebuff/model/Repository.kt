package android.com.moviebuff.model

import android.com.moviebuff.api.RetrofitFactory
import android.com.moviebuff.core.RemoteSource
import android.com.moviebuff.core.SingletonHolder

class Repository private constructor(retrofitFactory: RetrofitFactory) {

    private val fdsApiService: ApiService = retrofitFactory.getApiService(ApiService::class.java) as ApiService

    suspend fun getResponse(queryMap: Map<String, Any>) = RemoteSource.safeApiCall { fdsApiService.getApiRepoList(queryMap) }

    companion object : SingletonHolder<Repository, RetrofitFactory>(::Repository)
}
