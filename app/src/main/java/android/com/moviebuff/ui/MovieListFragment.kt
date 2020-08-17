package android.com.moviebuff.ui

import android.app.Application
import android.com.moviebuff.R
import android.com.moviebuff.core.InfiniteScrollListener
import android.com.moviebuff.core.ViewState
import android.com.moviebuff.ui.detail.MovieDetailActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_popular_movie.*

class MovieListFragment : BaseFeatureFragment(), AdapterCallbackInterface {
    private lateinit var adapter: MovieListAdapter
    private lateinit var viewModel: ListViewModel
    private var content: String? = null

    override fun getLayout() = R.layout.fragment_popular_movie

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        content = arguments?.getString("content") ?: "popular"
        viewModel =
            ViewModelProvider(this, ListViewModelFactory(content!!, activity!!.application)).get(
                ListViewModel::class.java
            )
        viewModel.moviesListLiveData.observe(viewLifecycleOwner, Observer {
            if (it is ViewState.Data) {
                loadDataIntoUi(it.data)
            }
        })
    }

    private fun loadDataIntoUi(data: MovieItemState) {
        if (!isAdded) return
        when (data) {
            is MovieItemState.FragmentData -> {
                adapter.submitList(data.list)
                rv.isVisible = data.list.isNotEmpty()
                emptyText.isVisible = data.list.isEmpty()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val infiniteScrollListener = object : InfiniteScrollListener() {
            override fun onLoadMore() {
                viewModel.loadMoreMovies()
            }

            override fun isDataLoading(): Boolean {
                val uiModel = viewModel.moviesListLiveData.value
                return uiModel is ViewState.Loading
            }
        }
        adapter = MovieListAdapter(this)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
//        rv.addOnScrollListener(infiniteScrollListener)

    }

    companion object {

        fun newInstance(content: String): MovieListFragment {
            val f = MovieListFragment()
            val args = Bundle()
            args.putString("content", content)
            f.arguments = args
            return f
        }
    }

    inner class ListViewModelFactory(private val type: String, application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass != ListViewModel::class.java) {
                throw IllegalArgumentException("Unknown ViewModel class")
            }

            return ListViewModel(type, activity!!.application) as T
        }
    }

    override fun cardClicked(id: Int) {
        startActivity(MovieDetailActivity.createIntent(context!!, id))
    }

}