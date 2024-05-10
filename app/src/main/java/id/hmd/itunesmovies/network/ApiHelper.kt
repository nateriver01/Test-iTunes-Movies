@file:Suppress("unused")

package id.hmd.itunesmovies.network

import android.content.Context
import com.google.gson.Gson
import id.hmd.itunesmovies.di.ApiModule
import id.hmd.itunesmovies.di.DaggerApiComponent
import id.hmd.itunesmovies.di.ServiceModule
import id.hmd.itunesmovies.model.response.MoviesListResponse
import id.hmd.itunesmovies.utils.AppConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


@Suppress("unused")
class ApiHelper
//    @Inject SharedPreferences sharedPreferences;
//    @Inject Encryption encryption;
//endregion

//region ApiHelper
private constructor(context: Context?) : ApiDataSource {

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var gson: Gson

    init {
        DaggerApiComponent.builder()
            .apiModule(ApiModule(AppConstants.BASE_URL))
            .serviceModule(ServiceModule())
            .build().inject(this)
    }

    companion object {
        //region Variable
        private var apiHelper: ApiHelper? = null

        @Synchronized
        fun getInstance(context: Context?): ApiHelper {
            if (apiHelper == null) {
                apiHelper = ApiHelper(context)
            }
            return apiHelper as ApiHelper
        }
    }

    override fun getMovies(callback: ApiDataSource.Callback<MoviesListResponse>):
            DisposableObserver<MoviesListResponse> {
        Timber.tag("check request").d(apiService.getMovies().toString())
        return apiService.getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribeWith(object : DisposableObserver<MoviesListResponse>(){
                override fun onNext(result: MoviesListResponse) {
                    callback.onSuccess(result)
                }

                override fun onError(e: Throwable) {
                    if(e is IOException){
                        callback.onConnectionError(e)
                    } else {
                        callback.onFailure(e)
                    }
                }

                override fun onComplete() {

                }

            })

    }

}


