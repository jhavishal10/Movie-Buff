package android.com.moviebuff.ui

sealed class MovieItemState {
    object Clear : MovieItemState()
    data class FragmentData(val list: List<ListItem>) : MovieItemState()
}