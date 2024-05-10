package id.hmd.itunesmovies.di

import id.hmd.itunesmovies.network.ApiService

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */

@Module
class ServiceModule {

    @Provides
    @Singleton
    internal fun provideApiService(@Named("provider_api") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}