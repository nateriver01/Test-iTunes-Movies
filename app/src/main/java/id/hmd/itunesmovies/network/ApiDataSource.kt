@file:Suppress("unused")

package id.hmd.itunesmovies.network

import id.hmd.itunesmovies.model.response.MoviesListResponse
import io.reactivex.rxjava3.observers.DisposableObserver


@Suppress("unused", "unused")
interface ApiDataSource {

    @Suppress("unused")
    interface Callback<T> {
        fun onSuccess(value: T)
        fun onFailure(e: Throwable)
        fun onConnectionError(e: Throwable)
    }

    //region get Catalog List
    fun getMovies(callback: Callback<MoviesListResponse>):
            DisposableObserver<MoviesListResponse>

}