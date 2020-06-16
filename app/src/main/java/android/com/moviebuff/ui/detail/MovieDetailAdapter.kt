package android.com.moviebuff.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_detail_information.view.*

class MovieDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<MovieDetailItem>()

    fun addItems(list: List<MovieDetailItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return InformationViewHolder(view)

    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InformationViewHolder).bind(list[position] as MovieDetailItem.Information)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class InformationViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(information: MovieDetailItem.Information) {
            with(itemView) {
                Glide.with(this)
                    .load(information.poster)
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(icon)
                movieOverviewText.text = information.overviewText
                stars.text = information.rating.toString() + " Rating"
                ratingScore.rating = information.rating
                budgetText.text = information.budget
                revenueText.text = information.revenue
                adult.isVisible = information.adult
                releaseDate.text = information.releaseDate

            }
        }
    }

}