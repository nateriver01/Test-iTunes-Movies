package id.hmd.itunesmovies.base

import id.hmd.itunesmovies.iTunesMoviesApp
import id.hmd.itunesmovies.network.ApiHelper
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Created by hmdrrhmn on 2019-09-23 at 15:37.
 */
open class BasePresenter {
    var disposable: Disposable? = null
    var apiHelper: ApiHelper = ApiHelper.getInstance(iTunesMoviesApp.instance)
}