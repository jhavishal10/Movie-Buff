package android.com.moviebuff.ui//package android.com.moviebuff.ui
//import android.com.moviebuff.R
//import androidx.annotation.LayoutRes
//import com.indwealth.common.recyclerview.AnalysisViewHolder
//import com.indwealth.common.recyclerview.SubTitleViewHolder
//import feature.whatsnew.R
//
//sealed class MovieListItem(@LayoutRes val viewType: Int) {
//    data class Information(val minimumInvestment: String, val startingDate: String, val endingDate: String, val exitLoad: String) : MovieListItem(R.layout.nfo_basic_information)
//    data class Objective(val objective: String) : MovieListItem(R.layout.nfo_fund_objective)
//    data class Advisory(val description: String) : MovieListItem(AnalysisViewHolder.getLayoutRes())
//    data class Risk(val risk: String) : MovieListItem(R.layout.nfo_risk_meter)
//}