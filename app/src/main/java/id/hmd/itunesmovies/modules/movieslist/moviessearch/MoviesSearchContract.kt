package id.hmd.itunesmovies.modules.movieslist.moviessearch

import id.hmd.itunesmovies.base.BaseView
import id.hmd.itunesmovies.model.response.ResultsItem

interface MoviesSearchContract {

    interface View : BaseView {
        fun onGetHistoryData(productList: List<String>?)
        fun onSuccessSearchProduct(moviesList : List<ResultsItem>)
        fun onDeleteAllHistory()
    }

    interface Presenter  {
        fun getHistoryData()
        fun saveHistoryData(historyList: List<String>, productTitle: String)
        fun deleteAllHistoryData()
    }
}