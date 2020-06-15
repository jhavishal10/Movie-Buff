package android.com.moviebuff.ui

import android.app.Application
import android.com.moviebuff.api.BaseApplication
import android.com.moviebuff.core.Result
import android.com.moviebuff.core.ViewState
import android.com.moviebuff.model.Repository
import androidx.lifecycle.*
import kotlinx.coroutines.launch


class ViewModel(application: Application) : AndroidViewModel(application) {

    private val fdsRepository: Repository by lazy {
        Repository.getInstance(getApplication<BaseApplication>().retrofitFactory)
    }
    private val queryMap = mutableMapOf<String, Any>()
    private var _searchFdsListLiveData = MutableLiveData<ViewState<String>>()
    val searchFdsListLiveData: LiveData<ViewState<String>>
        get() {
            return _searchFdsListLiveData
        }

    init {
        fetchFds()
    }

    private fun fetchFds() {
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"
        viewModelScope.launch {
            _searchFdsListLiveData.value = ViewState.Loading
            when (val result = fdsRepository.getResponse(queryMap)) {
                is Result.Success ->_searchFdsListLiveData.value = ViewState.Data("")
                is Result.Error -> _searchFdsListLiveData.value = ViewState.Error(result.error.message)
            }
        }
    }
}

