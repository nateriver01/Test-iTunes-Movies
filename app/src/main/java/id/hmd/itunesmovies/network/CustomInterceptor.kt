package id.hmd.itunesmovies.network

import android.content.Context
import android.content.SharedPreferences
import id.hmd.itunesmovies.utils.AppConstants
import id.hmd.itunesmovies.utils.LocalPreferences
import okhttp3.Interceptor
import okhttp3.Response


class CustomInterceptor(var sharedPreferences: SharedPreferences, var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)

    }

}