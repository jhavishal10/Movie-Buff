package android.com.moviebuff.ui.detail

import android.app.Application
import android.com.moviebuff.R
import android.com.moviebuff.core.ViewState
import android.com.moviebuff.ui.CoreActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailActivity : CoreActivity() {

    private val id by lazy { intent.getIntExtra("id", 1) }

    private lateinit var adapter: MovieDetailAdapter
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.hide()
        initUi()
    }

    private fun initUi() {

        adapter = MovieDetailAdapter()
        movieDetailRv.layoutManager = LinearLayoutManager(this)
        movieDetailRv.itemAnimator = null
        movieDetailRv.adapter = adapter
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, MovieDetailViewModelFactory(id, application)).get(
            MovieDetailViewModel::class.java
        )
        viewModel.movieDetailLiveData.observe(this, Observer<ViewState<MovieDetailState>> {
            when (it) {
                is ViewState.Loading -> showProgress()
                is ViewState.Error -> {
                    showLongToast(it.error)
                    hideProgress()
                }
                is ViewState.Data -> loadUiBasedOnData(it.data)
            }
        })
    }

    private fun loadUiBasedOnData(data: MovieDetailState) {
        hideProgress()
        when (data) {
            is MovieDetailState.Title -> toolBar.text = data.title
            is MovieDetailState.Data -> adapter.addItems(data.list)
        }
    }

    inner class MovieDetailViewModelFactory(private val fdId: Int, application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass != MovieDetailViewModel::class.java) {
                throw IllegalArgumentException("Unknown ViewModel class")
            }

            return MovieDetailViewModel(fdId, application) as T
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, id: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).putExtra("id", id)
        }
    }

}