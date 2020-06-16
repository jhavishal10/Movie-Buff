package android.com.moviebuff.ui.detail

import android.app.Application
import android.com.moviebuff.api.BaseApplication
import android.com.moviebuff.core.ViewState
import android.com.moviebuff.model.MovieDetailResponse
import android.com.moviebuff.model.Repository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

class MovieDetailViewModel(private val movieId: Int, application: Application) :
    AndroidViewModel(application) {
    private val repository: Repository by lazy {
        Repository.getInstance(getApplication<BaseApplication>().retrofitFactory)
    }

    private val queryMap = mutableMapOf<String, Any>()
    private var _movieDetailLiveData = MutableLiveData<ViewState<MovieDetailState>>()

    val movieDetailLiveData: LiveData<ViewState<MovieDetailState>>
        get() {
            return _movieDetailLiveData
        }

    init {
        getMovieDetail()
    }

    private fun getMovieDetail() {
        queryMap["language"] = "en-US"
        viewModelScope.launch {
            _movieDetailLiveData.value = ViewState.Loading
            when (val result = repository.getMovieDetail(movieId, queryMap)) {
                is android.com.moviebuff.core.Result.Success -> loadDataIntoUi(result.data)
                is android.com.moviebuff.core.Result.Error -> _movieDetailLiveData.value =
                    ViewState.Error(result.error.message)
            }
        }
    }

    private fun loadDataIntoUi(data: MovieDetailResponse) {
        addFdDetails(data)
        _movieDetailLiveData.value =
            ViewState.Data(MovieDetailState.Title(data.originalTitle ?: "Movie"))
    }

    private fun addFdDetails(data: MovieDetailResponse) {
        val list = mutableListOf<MovieDetailItem>()
        list.add(
            MovieDetailItem.Information(
                poster = "https://image.tmdb.org/t/p/w500" + data.backdropPath,
                rating = data.voteAverage ?: 0.0f,
                releaseDate = "Released On " + data.releaseDate,
                adult = data.adult == true,
                budget = "Production Cost is " + data.budget.shortDollarString(),
                status = data.status ?: "",
                revenue = "Revenue Generated " + data.revenue.shortDollarString(),
                overviewText = data.overview ?: ""
            )
        )



        _movieDetailLiveData.value = ViewState.Data(MovieDetailState.Data(list))
    }

    fun Number?.shortDollarString(hideThousands: Boolean = true): String {
        if (this == null) return ""
        val value = toDouble() // forget decimals in short form
        val absolute = abs(value)
        if (absolute < 1e3) return absolute.dollarString()
        if (absolute >= 1e3 && absolute < 1e6) {
            return if (hideThousands) "$" + (absolute / 1e3).toFixed(2) + "K"
            else {
                if (absolute < 1e4) {
                    absolute.dollarString()
                } else {
                    "$" + (absolute / 1e3).toFixed(2) + "K"
                }
            }
        }
        if (absolute >= 1e6 && absolute < 1e9) return "$" + (absolute / 1e6).toFixed(2) + "M"
        if (absolute >= 1e9 && absolute < 1e12) return "$" + (absolute / 1e9).toFixed(2) + "B"
        if (absolute >= 1e12) return "$" + (absolute / 1e12).toFixed(1) + "T"
        return "$0"
    }

    private fun Number?.dollarString(): String {
        if (this == null) return ""
        return toDollar(this)
    }

    fun Double.toFixed(decimals: Int): String {
        if (this == 0.0) return "0"
        val factor = 10.0.pow(decimals.toDouble())
        return ((this * factor).roundToInt() / factor).toString()
    }

    fun toDollar(number: Number?): String {
        return if (number == null) "$0" else NumberFormat.getCurrencyInstance(Locale.US)
            .format(number)
    }

    fun getMovieVideo() {
        viewModelScope.launch {
            when (val result = repository.getMovieVideo(movieId, queryMap)) {
                is android.com.moviebuff.core.Result.Success -> {
                    result.data.results?.forEach {
                        if (it?.site == "YouTube") {
                            _movieDetailLiveData.value = ViewState.Data(MovieDetailState.Video(it.key!!))
                            return@launch
                        }
                    }
                }
                is android.com.moviebuff.core.Result.Error -> _movieDetailLiveData.value =
                    ViewState.Error(result.error.message)
            }
        }
    }


}
