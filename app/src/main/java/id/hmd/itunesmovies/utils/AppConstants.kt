@file:Suppress("unused")

package id.hmd.itunesmovies.utils

import id.hmd.itunesmovies.BuildConfig


/**
 * Created by hmdrrhmn on 2019-10-03 at 17:37.
 */
@Suppress("unused")
object AppConstants {

    init {
        System.loadLibrary("native-lib")
    }



    private val environmentStage =BuildConfig.STAGE_TYPE

    private external fun appUrl(environmentStage: Int): String
    private external fun hostName(environmentStage: Int): String
    private external fun dbPassword(environmentStage: Int): String
    private external fun getPreferenceName(environmentStage: Int): String
    private external fun getSharedPreferencePassword(environmentStage: Int): String
    private external fun getDbName(environmentStage: Int): String

    val BASE_URL = appUrl(environmentStage)
    val PREF_NAME: String = getPreferenceName(environmentStage)


    const val PRODUCT_CATALOG_ID = "PRODUCT_CATALOG_ID"
    const val FILTER_PRODUCT_TITLE = "FILTER_PRODUCT_TITLE"

    const val PREF_SEARCHHISTORY = "PREF_SEARCHHISTORY"


}