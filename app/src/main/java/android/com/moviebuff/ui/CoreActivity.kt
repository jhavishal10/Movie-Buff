package android.com.moviebuff.ui

import android.app.Activity
import android.com.moviebuff.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_base.*
import okhttp3.HttpUrl
import timber.log.Timber

abstract class CoreActivity : AppCompatActivity() {

    open val lightNavBar = true
    open val lightStatusBar = false

    /**
     * @return true (Default) to check if activity is Home Screen.
     */
    open val openHomeIfEmptyBackStack = true

    @MenuRes
    open val menu: Int? = null
    private var progressShowing = false
    private var cancellable = false

    val uiHandler = Handler(Looper.getMainLooper())
    private var userLoggedOut = false

    /**
     * Use [androidx.activity.viewModels] instead
     */
    @Deprecated("Use androidx.activity.viewModels")
    open fun initViewModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
        if (lightStatusBar) {
            setLightStatusBar()
        } else {
            clearLightStatusBar()
        }
        initFullScreenFlag()
        initViewModel()
    }


    private fun initFullScreenFlag() {
        if (needFullScreen()) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun setContentView(layoutResID: Int) {
        if (shouldOverrideContentView()) {
            val view = layoutInflater.inflate(layoutResID, baseContainerFrame, false)
            if (view != null) {
                baseContainerFrame.removeAllViews()
                baseContainerFrame.addView(view)
            }
        } else {
            super.setContentView(layoutResID)
        }
    }

    override fun setContentView(view: View?) {
        if (shouldOverrideContentView()) {
            if (view != null) {
                baseContainerFrame.removeAllViews()
                baseContainerFrame.addView(view)
            }
        } else {
            super.setContentView(view)
        }
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        if (shouldOverrideContentView()) {
            if (view != null) {
                baseContainerFrame.removeAllViews()
                baseContainerFrame.addView(view)
            }
        } else {
            super.setContentView(view, params)
        }
    }

    override fun onCreateOptionsMenu(m: Menu): Boolean {
        if (menu == null) return false
        menuInflater.inflate(menu!!, m)
        return true
    }

    override fun onBackPressed() {
        if (progressShowing) {
            if (cancellable) {
                checkStackAndFinishActivity()
            }
        } else {
            checkStackAndFinishActivity()
        }
    }

    private fun checkStackAndFinishActivity() {
        if (supportFragmentManager.isStateSaved || !supportFragmentManager.popBackStackImmediate()) {
            if (isTaskRoot && openHomeIfEmptyBackStack) {
                clearStackAndOpenHome()
            } else {
                finishAfterTransition()
            }
        }
    }

    fun clearStackAndOpenHome(deepLink: String = "dashboard") {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(getString(R.string.deeplink_host))
            .addPathSegment(deepLink)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
        intent.`package` = packageName
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finishAfterTransition()
    }


    open fun shouldOverrideContentView(): Boolean {
        return true
    }

    /**
     * @return true (Default) to enable log out listener which would start Login screen if user is
     * logged-out because of any reason.
     */
    open fun needLogOutListener(): Boolean {
        return true
    }

    /**
     * @return true (Default) to add full screen flag and override status bar draw.
     */
    open fun needFullScreen(): Boolean {
        return true
    }

    /**
     * @return true (Default) to show key guard when this activity is resume
     * false to show directly
     */
    open var shouldShowKeyGuard = { true }

    fun go(activity: Class<out Activity>) {
        startActivity(Intent(this, activity))
    }

    fun setLightStatusBar() {
        Timber.d("theme setLightStatusBar")
        val view: View = findViewById(android.R.id.content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }
    }

    fun clearLightStatusBar() {
        Timber.d("theme clearLightStatusBar")
        val view: View = findViewById(android.R.id.content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            view.systemUiVisibility = flags
        }
    }

    open fun showProgress(message: String = "Loading...", cancellable: Boolean = false) {
        if (!shouldOverrideContentView()) return
        progressCard.isVisible = true
        progressShowing = true
        progressText.text = message
        this.cancellable = cancellable
        if (!cancellable) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    open fun hideProgress() {
        if (!shouldOverrideContentView()) return

        progressShowing = false
        progressCard.isVisible = false
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            uiHandler.postDelayed({
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }, 200)
        }
    }

    fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }
}