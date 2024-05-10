package id.hmd.itunesmovies.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */

fun Activity.changePage(destinationPage: Class<*>, bundle: Bundle? = null) {
    changePage(destinationPage, bundle, false)
}

fun Activity.changePage(destinationPage: Class<*>, bundle: Bundle? = null, finishActivity: Boolean) {
    val intent = Intent(this, destinationPage)
    if (bundle != null)
        intent.putExtras(bundle)
    startActivity(intent)
    if (finishActivity) finish()
}

fun FragmentManager.replace(container: Int, fragment: Fragment, addToBackStack: Boolean) {
    val transaction = this.beginTransaction()
    transaction.replace(container, fragment)
    if (addToBackStack)
        transaction.addToBackStack(fragment.javaClass.name)
    //error occur when call transaction.commit()
    //so I change it to commitAllowingStateLoss
    transaction.commitAllowingStateLoss()
}

fun Long.currencyFormatterLong(): String {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.ITALY) as DecimalFormat
    decimalFormat.applyPattern("###,###.##")
    return decimalFormat.format(this)
}
fun String.digitsOnly(): String{
    val regex = Regex("[^0-9]")
    return regex.replace(this, "")
}


fun View.toggleRotation(isExpand:Boolean) {

    if(isExpand){
        animate().rotation(0f).start()
    }else{
        animate().rotation(-180f).start()
    }
}

fun View.toggleVisibility() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}