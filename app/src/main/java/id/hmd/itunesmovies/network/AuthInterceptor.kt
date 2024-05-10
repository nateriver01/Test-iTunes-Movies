package id.hmd.itunesmovies.network

import android.content.Context
import android.content.SharedPreferences
import id.hmd.itunesmovies.utils.LocalPreferences
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(var sharedPreferences: SharedPreferences, var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val localPreferencesNew = LocalPreferences(sharedPreferences)

        return response
    }

}