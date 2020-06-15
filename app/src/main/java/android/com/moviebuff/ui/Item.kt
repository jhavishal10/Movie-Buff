package android.com.moviebuff.ui

import android.com.moviebuff.R
import androidx.annotation.LayoutRes


sealed class DetailItem(@LayoutRes val viewType: Int) {
 data class Item(
  val name: String, val description: String, val owner: String,
  val ownerImage: String
 ) : DetailItem(R.layout.list_layout)
}