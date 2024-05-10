@file:Suppress("unused")
package id.hmd.itunesmovies.network

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

@Suppress("unused")
class AuthInterceptor(var sharedPreferences: SharedPreferences, var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return response
    }

}