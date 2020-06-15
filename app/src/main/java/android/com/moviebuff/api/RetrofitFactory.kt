package android.com.moviebuff.api

import android.com.moviebuff.BuildConfig
import android.content.Context
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitFactory(private val context: Context, baseUrl: String, private var defaultTimeOut: Long = 60) {

    private val nullOnEmptyConverterFactory by lazy {
        object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
    }

    private val apiLogLevel =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

    private val retrofit by lazy {
        createRetrofitInstance(baseUrl)
    }

    private val apiMap: MutableMap<String, Any> = mutableMapOf()

    private fun createRetrofitInstance(baseUrl: String): Retrofit {
        val httpClientBuilder = getOkHttpClientBuilder()

        val retroBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClientBuilder.build())

        getConverterFactory().forEach {
            retroBuilder.addConverterFactory(it)
        }
        return retroBuilder.build()
    }

    /**
     * Provide the OkHttpBuilder with all the basic logging. Override to change the default builder or add any property.
     * @return OkHttpBuilder to be passed in retrofit builder
     */
    open fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = apiLogLevel

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(logging)
            .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
            .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
            .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
    }


    /**
     * Provide converter factory. And override to provide converter factory according to App requirements.
     *
     * @return Converter Factory
     */
    open fun getConverterFactory(): List<Converter.Factory> {
        return listOf(nullOnEmptyConverterFactory, GsonConverterFactory.create())
    }

    /**
     * Get the API Service instance for given class
     *
     * @param apiClass API interface class
     * @return api object reference to call Retrofit mode APIs
     */
    fun <T> getApiService(apiClass: Class<T>): Any? {
        var api: Any? = apiMap[apiClass.name]
        if (api == null) {
            api = retrofit.create(apiClass)
            apiMap[apiClass.name] = api!!
        }
        return api
    }
}