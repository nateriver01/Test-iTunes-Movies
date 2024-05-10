package id.hmd.itunesmovies.modules.movies

import id.hmd.itunesmovies.base.BaseView
import id.hmd.itunesmovies.model.response.ResultsItem


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:48.
 */
interface MoviesContract {
    interface View : BaseView {
        fun onSuccessgetMoviesList(moviesList: List<ResultsItem>?)
        fun onFailGetListDataMovies()
    }

    interface Presenter  {
        fun getMoviesList()
    }
}