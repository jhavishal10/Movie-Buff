package android.com.moviebuff.api

import androidx.multidex.MultiDexApplication

abstract class BaseApplication : MultiDexApplication() {

    open lateinit var retrofitFactory: RetrofitFactory
    open lateinit var uploadFactory: RetrofitFactory

    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    open fun initDependencies() {
        createRetrofitFactory()
    }

    open fun createRetrofitFactory() {
        retrofitFactory =
            RetrofitFactory(this, getBaseURL())
        uploadFactory =
            RetrofitFactory(this, getBaseURL(), 50)
    }

    abstract fun getBaseURL(): String
}