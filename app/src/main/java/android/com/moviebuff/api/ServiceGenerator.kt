package android.com.moviebuff.api

import android.app.Application
import android.com.moviebuff.BuildConfig
import android.com.moviebuff.api.RetrofitFactory
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient


class ServiceGenerator(private val context: Application, defaultTimeOut: Long = 40) : RetrofitFactory(context, BuildConfig.BASE_URL, defaultTimeOut) {

    override fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpBuilder = super.getOkHttpClientBuilder()
        okHttpBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("version", BuildConfig.VERSION_NAME)
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
                request.url(url)
            return@addInterceptor chain.proceed(request.build())
        }

        okHttpBuilder.addInterceptor(ChuckerInterceptor(context))

        return okHttpBuilder
    }
}