package id.hmd.itunesmovies.modules.myfav

import id.hmd.itunesmovies.base.BaseView
import id.hmd.itunesmovies.model.response.ResultsItem


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:48.
 */
interface MyFavContract {
    interface View : BaseView {
        fun onSuccessGetMyFavList(myfavList: List<ResultsItem>)
        fun onFailedGetMyFavList()
        fun onSuccessOrderCount(count: Int)
        fun onFailOrderCount()
    }

    interface Presenter
}