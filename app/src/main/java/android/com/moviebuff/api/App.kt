package android.com.moviebuff.api

import android.com.moviebuff.BuildConfig
import timber.log.Timber

class App : BaseApplication() {

    override fun getBaseURL(): String {
        return BuildConfig.BASE_URL
    }

    override fun onCreate() {

        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, "xxx_" + tag!!, message, t)
                }
            })
        }
    }

    override fun createRetrofitFactory() {
        retrofitFactory = ServiceGenerator(this)
        uploadFactory = ServiceGenerator(this, 60)
    }
}