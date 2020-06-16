package android.com.moviebuff.ui.detail

sealed class MovieDetailState {
    object Clear : MovieDetailState()
    data class Title(val title: String) : MovieDetailState()
    data class Data(val list: List<MovieDetailItem>) : MovieDetailState()
}