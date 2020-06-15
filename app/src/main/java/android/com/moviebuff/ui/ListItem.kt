package android.com.moviebuff.ui

import androidx.recyclerview.widget.DiffUtil

data class ListItem(
    val id: Int,
    val name: String,
    val icon: String,
    val overviewText: String,
    val score: Float
) {
    companion object {

        val DIFF_UTIL = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}