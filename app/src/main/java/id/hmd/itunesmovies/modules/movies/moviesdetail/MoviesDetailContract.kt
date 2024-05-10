@file:Suppress("RemoveEmptyClassBody")

package id.hmd.itunesmovies.modules.movies.moviesdetail

import id.hmd.itunesmovies.base.BaseView
import id.hmd.itunesmovies.model.response.ResultsItem


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:48.
 */
interface MoviesDetailContract {
    interface View : BaseView {
        fun onSuccessGetProduct(product:ResultsItem)
        fun onFailRedeem()
        fun onFailGetProduct()
    }

    interface Presenter  {
    }
}