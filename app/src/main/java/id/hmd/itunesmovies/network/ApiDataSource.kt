package id.hmd.itunesmovies.network

import id.hmd.itunesmovies.model.response.MoviesListResponse
import io.reactivex.rxjava3.observers.DisposableObserver


interface ApiDataSource {

    interface Callback<T> {
        fun onSuccess(value: T)
        fun onFailure(e: Throwable)
        fun onConnectionError(ignoredE: Throwable)
    }

    //region get Catalog List
    fun getMovies(callback: Callback<MoviesListResponse>):
            DisposableObserver<MoviesListResponse>

}