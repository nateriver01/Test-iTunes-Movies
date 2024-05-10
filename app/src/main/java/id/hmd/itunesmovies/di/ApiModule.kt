@file:Suppress("LocalVariableName")

package id.hmd.itunesmovies.di


import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import id.hmd.itunesmovies.iTunesMoviesApp
import id.hmd.itunesmovies.network.AuthInterceptor
import id.hmd.itunesmovies.network.CustomInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import id.hmd.itunesmovies.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */
@Module
class ApiModule(private val apiUrl: String) {

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }


    @Provides
    @Singleton
    internal fun provideSharedPref(): SharedPreferences {
        return iTunesMoviesApp.instance.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    internal fun provideActivity(): Context {
        return iTunesMoviesApp.instance
    }

    @Provides
    @Singleton
    internal fun provideInterceptor(sharedPreferences: SharedPreferences, context: Context): CustomInterceptor {
        return CustomInterceptor(sharedPreferences,context)
    }

    @Provides
    @Singleton
    internal fun provideAuthInterceptor(sharedPreferences: SharedPreferences, context: Context): AuthInterceptor {
        return AuthInterceptor(sharedPreferences,context)
    }


    //////////////////

    @Provides
    @Singleton
    @Named("provider_api")
    internal fun provideOkHttpClient(ignoredCustomInterceptor: CustomInterceptor, ignoredAuthInterceptor: AuthInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(iTunesMoviesApp.instance))

        return builder.build()
    }

    @Suppress("LocalVariableName")
    @Provides
    @Singleton
    @Named("provider_api")
    internal fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        RxJava3CallAdapterFactory: RxJava3CallAdapterFactory, @Named("provider_api") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava3CallAdapterFactory)
            .baseUrl(apiUrl)
            .client(okHttpClient).build()
    }

}