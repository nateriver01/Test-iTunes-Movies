package id.hmd.itunesmovies.modules.movies.moviessearch

import android.content.Context
import id.hmd.itunesmovies.iTunesMoviesApp
import id.hmd.itunesmovies.base.BasePresenter
import id.hmd.itunesmovies.utils.AppConstants
import id.hmd.itunesmovies.utils.LocalPreferences

class MoviesSearchPresenter(
    private val view: MoviesSearchContract.View
) : BasePresenter(), MoviesSearchContract.Presenter {

    private val sharedPref = iTunesMoviesApp.instance.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
    private val localPreferences = LocalPreferences(sharedPref)

    override fun getHistoryData() {
        val mData = localPreferences.getPrefrencesByPrefix(AppConstants.PREF_SEARCHHISTORY)
        view.onGetHistoryData(mData)
    }

    override fun deleteAllHistoryData() {
        localPreferences.deletePrefrences(AppConstants.PREF_SEARCHHISTORY)
        view.onDeleteAllHistory()
    }

    override fun saveHistoryData(historyList: List<String>, productTitle: String) {
        if(!historyList.contains(productTitle)){
            val temp = mutableListOf<String>()
            temp.add(productTitle)
            temp.addAll(historyList)
            localPreferences.deletePrefrences(AppConstants.PREF_SEARCHHISTORY)
            if(temp.size > 3){
                localPreferences.savePrefrencesByPrefix(AppConstants.PREF_SEARCHHISTORY, temp.take(3))
            } else {
                localPreferences.savePrefrencesByPrefix(AppConstants.PREF_SEARCHHISTORY, temp)
            }
        }
    }

}