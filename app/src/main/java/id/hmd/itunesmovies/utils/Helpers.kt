package id.hmd.itunesmovies.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import id.hmd.itunesmovies.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.hmd.itunesmovies.databinding.BottomsheetErrorBinding
import com.pawegio.kandroid.layoutInflater


object Helpers {

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

    val emojiFilter = InputFilter{
            source: CharSequence, start: Int, end: Int, _: Spanned, _: Int, _: Int ->

        for(i in start until end){
            val type  = Character.getType(source[i])
            if(type==Character.SURROGATE.toInt()||type==Character.OTHER_SYMBOL.toInt()){
                return@InputFilter ""
            }
        }
        null
    }

}