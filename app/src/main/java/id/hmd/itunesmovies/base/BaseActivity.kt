package id.hmd.itunesmovies.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.hmd.itunesmovies.iTunesMoviesApp
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.di.ApiModule
import id.hmd.itunesmovies.di.DaggerApiComponent
import id.hmd.itunesmovies.di.ServiceModule
import id.hmd.itunesmovies.utils.AppConstants
import id.hmd.itunesmovies.utils.Helpers
import id.hmd.itunesmovies.utils.LocalPreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


abstract class BaseActivity: AppCompatActivity(), BaseView {

    @Inject
    lateinit var sharedPref: SharedPreferences

    lateinit var localPreferences: LocalPreferences

    protected abstract val binding: ViewBinding

    lateinit var viewModel : ViewModel

    lateinit var coroutineExcHandler: CoroutineExceptionHandler

    private lateinit var bottsheetOffline: BottomSheetDialog
    private lateinit var bottsheetError: BottomSheetDialog

    //protected abstract fun getViewBind():ViewBinding
    protected abstract fun onBindView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerApiComponent.builder()
            .apiModule(ApiModule(AppConstants.BASE_URL))
            .serviceModule(ServiceModule())
            .build().inject(this)

        setContentView(binding.root)

        localPreferences = LocalPreferences(sharedPref)
        initBottSheetOffline()
        initBottSheetGeneralError()

        initExceptionHandler()

        onBindView()
    }

    protected fun dismissKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showInetError() {
        if (::bottsheetOffline.isInitialized) {
            if (!iTunesMoviesApp.isOfflineShowing) {
                bottsheetOffline.show()
            }
        }
    }

    override fun showGeneralError(errMessage: String) {

        if (::bottsheetError.isInitialized) {
            if (!iTunesMoviesApp.isOfflineShowing) {
                initBottSheetGeneralError("Error", errMessage)
                bottsheetError.show()
            }
        }
    }

    private fun initBottSheetOffline() {
        bottsheetOffline = Helpers.bottomSheetError(
            mContext = this,
            //showButton = true,
            title = resources.getString(R.string.all_offlinetitle),
            desc = resources.getString(R.string.all_offlinedesc),
            placeholderImg = R.drawable.ic_offline,
            onRetry = {
                this.onRetryClicked()
                it.dismiss()
            },
            onClose = {
                it.dismiss()
                this.onCloseClicked()
            })
        bottsheetOffline.setOnShowListener {
            iTunesMoviesApp.isOfflineShowing = true
        }
        bottsheetOffline.setOnDismissListener {
            iTunesMoviesApp.isOfflineShowing = false
        }
    }

    private fun initBottSheetGeneralError(title: String = "", message: String = "") {
        bottsheetError = Helpers.bottomSheetError(
            mContext = this,
//            showButton = true,
            title = title,
            desc = message,
            placeholderImg = R.drawable.ic_errorall,
            onRetry = {
//                this.onRetryClicked()
                it.dismiss()
            },
            onClose = {
                it.dismiss()
                this.onCloseClicked()
            })
        bottsheetError.setOnShowListener {
            iTunesMoviesApp.isOfflineShowing = true
        }
        bottsheetError.setOnDismissListener {
            iTunesMoviesApp.isOfflineShowing = false
        }
    }

    private fun initExceptionHandler(){
        coroutineExcHandler = CoroutineExceptionHandler { _, throwable ->
            CoroutineScope(Dispatchers.Main).launch {
                when (throwable) {
                    is IOException -> { // onConnectionError(e: Throwable)
                        showInetError()
                    }
                    else -> { // onFailure(e: Throwable)
                        val message = throwable.message ?: "Unknown error"
                        showGeneralError(message)
                    }
                }
            }
        }
    }


    override fun onRetryClicked() {
        println("onRetryClicked BaseActivity")
    }

    override fun onCloseClicked() {
        println("onRetryClicked BaseActivity")
    }
}