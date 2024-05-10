package id.hmd.itunesmovies.modules.movies

import id.hmd.itunesmovies.base.BasePresenter
import id.hmd.itunesmovies.model.response.MoviesListResponse
import id.hmd.itunesmovies.network.ApiDataSource


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:48.
 */
class MoviesPresenter(private val view: MoviesContract.View) : BasePresenter(), MoviesContract.Presenter {

    override fun getMoviesList() {
        disposable = apiHelper.getMovies(object : ApiDataSource.Callback<MoviesListResponse> {
            override fun onSuccess(value: MoviesListResponse) {
                val catalogList = value.results
                if (catalogList != null) {
                    if (catalogList.isNotEmpty()) {
                        view.onSuccessgetMoviesList(catalogList)
                    } else {
                        view.onFailGetListDataMovies()
                    }
                } else {
                    view.onFailGetListDataMovies()
                }
            }

            override fun onFailure(e: Throwable) {
                view.onFailGetListDataMovies()
                view.showGeneralError(e.message.toString())
//                Toast.makeText(AstrakuApp.instance, e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onConnectionError(e: Throwable) {
                view.onFailGetListDataMovies()
                view.showInetError()
            }

        })
    }


}