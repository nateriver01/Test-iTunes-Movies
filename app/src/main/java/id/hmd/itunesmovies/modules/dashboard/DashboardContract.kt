@file:Suppress("unused")
package id.hmd.itunesmovies.modules.dashboard

import id.hmd.itunesmovies.base.BaseView


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:48.
 */
@Suppress("unused")
interface DashboardContract {
    interface View : BaseView {
        fun setTabLayoutGrey(on: Boolean)
    }

    interface Presenter  {
        fun setDeviceToken(token:String)
    }
}