package id.hmd.itunesmovies.base

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject


/**
 * Created by hmdrrhmn on 2019-09-23 at 15:39.
 */

abstract class BaseFragment: Fragment(), BaseView{

    @Inject
    lateinit var sharedPref: SharedPreferences

    lateinit var localPreferences: LocalPreferences

    private lateinit var bottsheetOffline: BottomSheetDialog
    private lateinit var bottsheetError: BottomSheetDialog

    protected abstract fun getViewBind(container: ViewGroup?): ViewBinding
    protected abstract fun onBindView()

    protected lateinit var fragmentHandler: Handler

    lateinit var coroutineExcHandler: CoroutineExceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerApiComponent.builder()
            .apiModule(ApiModule(AppConstants.BASE_URL))
            .serviceModule(ServiceModule())
            .build().inject(this)

        localPreferences = LocalPreferences(sharedPref)
        initBottSheetOffline()
        initBottSheetGeneralError()
        activity?.mainLooper?.let { fragmentHandler = Handler(it) }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(getFragmentView(), container, false)
        return getViewBind(container).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onBindView()
    }

    override fun showInetError() {
        if(::bottsheetOffline.isInitialized){
            if(!iTunesMoviesApp.isOfflineShowing){
                if(activity!=null) bottsheetOffline.show()
            }
        }
    }

    override fun showGeneralError(errMessage: String) {
        if (::bottsheetError.isInitialized) {
            if (!iTunesMoviesApp.isOfflineShowing && !bottsheetError.isShowing && context !=null) {

                initBottSheetGeneralError("General Error", errMessage)
                bottsheetError.show()
            }
        }
    }



    private fun initBottSheetOffline(){
        bottsheetOffline = Helpers.bottomSheetError(
            mContext = requireContext(),
//            showButton = true,
            title = resources.getString(R.string.all_offlinetitle),
            desc = resources.getString(R.string.all_offlinedesc),
            placeholderImg = R.drawable.ic_offline,
            onRetry = {
                this.onRetryClicked()
                it.dismiss()
            },
            onClose = {
                it.dismiss()
                onCloseClicked()
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
            mContext = requireContext(),
            title = title,
            desc = message,
            placeholderImg = R.drawable.ic_errorall,
            onRetry = {
                it.dismiss()
            },
            onClose = {
                it.dismiss()
                onCloseClicked()
            })
        bottsheetError.setOnShowListener {
            iTunesMoviesApp.isOfflineShowing = true
        }
        bottsheetError.setOnDismissListener {
            iTunesMoviesApp.isOfflineShowing = false
        }
    }



    override fun onRetryClicked() {
        println("onRetryClicked BaseFragment")
    }

    override fun onCloseClicked() {
        println("onRetryClicked BaseFragment")
    }

}