package id.hmd.itunesmovies.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import id.hmd.itunesmovies.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pawegio.kandroid.layoutInflater
import id.hmd.itunesmovies.databinding.BottomsheetErrorBinding

import java.util.*


object Helpers {
    fun showToast(context: Context, message: String) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

    @SuppressLint("InflateParams")
    fun bottomSheetError(
        mContext: Context,
        placeholderImg: Int = R.drawable.ic_notregistered,
        title: String = "",
        desc: String = "",
        showButton : Boolean = false,
        onRetry: ((BottomSheetDialog) -> Unit)?,
        onClose: ((BottomSheetDialog) -> Unit)?
    ): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(mContext)
        //val sheetViewnotRegistered = mContext.layoutInflater?.inflate(R.layout.bottomsheet_error, null)
        val sheetViewnotRegistered = mContext.layoutInflater?.let {
            BottomsheetErrorBinding.inflate(
                it
            )
        }
        sheetViewnotRegistered?.clNoconnectionButton?.visibility =
            if(showButton) { View.VISIBLE} else {View.GONE}

        sheetViewnotRegistered?.ivNoconnectionImgplaceholder?.setImageResource(placeholderImg)
        sheetViewnotRegistered?.tvNoconnectionTitle?.text = title
        sheetViewnotRegistered?.tvNoconnectionDesc?.text = desc

        sheetViewnotRegistered?.btnNoconnectionButton?.setOnClickListener {
            onRetry?.invoke(
                bottomSheetDialog
            )
        }
        sheetViewnotRegistered?.ivNoconnectionDismiss?.setOnClickListener {
            onClose?.invoke(
                bottomSheetDialog
            )
        }
        sheetViewnotRegistered?.let { bottomSheetDialog.setContentView(it.root) }
        return bottomSheetDialog
    }

    fun createCircularProgress(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    fun grayScalingImage(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        return filter
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    val emojiFilter = InputFilter{
            source: CharSequence, start: Int, end: Int, spanned: Spanned, i2: Int, i3: Int ->

        for(i in start until end){
            val type  = Character.getType(source.get(i))
            if(type==Character.SURROGATE.toInt()||type==Character.OTHER_SYMBOL.toInt()){
                return@InputFilter ""
            }
        }
        null
    }



}