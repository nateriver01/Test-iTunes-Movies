package id.hmd.itunesmovies.di

//import com.google.firebase.messaging.FirebaseMessagingService

import dagger.Component
import id.hmd.itunesmovies.base.BaseActivity
import id.hmd.itunesmovies.base.BaseFragment
import id.hmd.itunesmovies.network.ApiHelper
import javax.inject.Singleton

/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */

@Singleton
@Component(modules = [ApiModule::class, ServiceModule::class])
interface ApiComponent {
    fun inject(apiHelper: ApiHelper)
    fun inject(activity: BaseActivity)
    fun inject(fragment: BaseFragment)

}