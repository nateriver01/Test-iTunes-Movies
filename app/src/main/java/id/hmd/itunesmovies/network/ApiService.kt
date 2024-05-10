package id.hmd.itunesmovies.network

import id.hmd.itunesmovies.model.response.MoviesListResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET


/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */

interface ApiService {

    @GET("/search?term=star&country=au&media=movie&limit=50")
    fun getMovies(): Observable<MoviesListResponse>

}