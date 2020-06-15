package android.com.moviebuff.ui

import android.com.moviebuff.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import okhttp3.HttpUrl

abstract class BaseFeatureFragment : Fragment() {

    abstract fun getLayout(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(getLayout(), container, false)
    }

    /**
     * Show Progress Dialog
     *
     * @param message message to show
     * @param cancellable dialog cancellable
     */
    fun showProgress(message: String = "Loading...", cancellable: Boolean = false) {
        (activity as CoreActivity).showProgress(message, cancellable)
    }

    /**
     * clears the stack above root home activity and opens
     */
    fun clearStackAndOpenDashboard(deepLink: String = "dashboard") {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(getString(R.string.deeplink_host))
            .addPathSegment(deepLink)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
        intent.`package` = requireContext().packageName
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    fun hideProgress() {
        (activity as CoreActivity).hideProgress()
    }
}