package id.hmd.itunesmovies.base


/**
 * Created by hmdrrhmn on 2019-09-23 at 15:38.
 */
interface BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showInetError()
    fun showGeneralError(errMessage : String)
    fun onRetryClicked()
    fun onCloseClicked()
}