package android.com.moviebuff.ui

import android.com.moviebuff.R
import android.com.moviebuff.core.DebouncedOnClickListener
import android.com.moviebuff.ui.ListItem.Companion.DIFF_UTIL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_item.view.*

class MovieListAdapter(private val adapterCallbackInterface: AdapterCallbackInterface) :
    ListAdapter<ListItem, MovieListAdapter.MovieItemViewHolder>(DIFF_UTIL) {


    override fun getItemCount(): Int {
        return if (currentList == null) 0 else (currentList.size*2)
    }

    override fun getItem(position: Int): ListItem {
        val positionInList: Int = position % currentList.size
        return currentList[positionInList]
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MovieItemViewHolder(view, adapterCallbackInterface)
    }

    inner class MovieItemViewHolder(view: View, callback: AdapterCallbackInterface) :
        RecyclerView.ViewHolder(view) {

        init {
            itemView.debouncedOnClick {
                val listItem = it.tag as ListItem
                callback.cardClicked(listItem.id)
            }
        }

        fun bind(item: ListItem) {
            with(itemView) {
                tag = item
                name.text = item.name
                Glide.with(this)
                    .load(item.icon)
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(icon)
                movieOverviewText.text = item.overviewText
                ratingScore.rating = item.score
                stars.text = item.score.toString() + " Rating"
            }
        }
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

interface AdapterCallbackInterface {
    fun cardClicked(id: Int)
}

inline fun View.debouncedOnClick(debounceTill: Long = 500, crossinline onClick: (v: View) -> Unit) {
    this.setOnClickListener(object : DebouncedOnClickListener(debounceTill) {
        override fun onDebouncedClick(v: View) {
            onClick(v)
        }
    })
}
