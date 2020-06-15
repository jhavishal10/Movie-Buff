package android.com.moviebuff.ui

import android.app.Application
import android.com.moviebuff.api.BaseApplication
import android.com.moviebuff.core.Result
import android.com.moviebuff.core.ViewState
import android.com.moviebuff.model.PopularMovieResponse
import android.com.moviebuff.model.Repository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.collections.set

class ListViewModel(private val type: String, application: Application) : AndroidViewModel(application) {

    private val fdsRepository: Repository by lazy {
        Repository.getInstance(getApplication<BaseApplication>().retrofitFactory)
    }
    private val queryMap = mutableMapOf<String, Any>()
    private var _moviesListLiveData = MutableLiveData<ViewState<MovieItemState>>()
    val moviesListLiveData: LiveData<ViewState<MovieItemState>>
        get() {
            return _moviesListLiveData
        }

    init {
        fetchFds()
    }

    private fun fetchFds() {
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        viewModelScope.launch {
            _moviesListLiveData.value = ViewState.Loading
            when (val result = fdsRepository.getPopularMovieList(type, queryMap)) {
                is Result.Success -> addItemsToOverviewList(result.data)
                is Result.Error -> _moviesListLiveData.value = ViewState.Error(result.error.message)
            }
        }
    }

    private fun addItemsToOverviewList(data: PopularMovieResponse) {
        val list = mutableListOf<ListItem>()
        data.results?.forEach {
            list.add(
                ListItem(
                    name = it?.originalTitle ?: "",
                    id = it?.id ?: 0,
                    icon = "https://image.tmdb.org/t/p/w500" + (it?.backdropPath),
                    overviewText = it?.overview ?: "",
                    score = it?.voteAverage ?: 0f
                )
            )

        }
        _moviesListLiveData.value = ViewState.Data(MovieItemState.FragmentData(list))
    }


}

