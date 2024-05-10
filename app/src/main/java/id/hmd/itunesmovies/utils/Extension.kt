package id.hmd.itunesmovies.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import id.hmd.itunesmovies.R
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */

fun Activity.changePage(destinationPage: Class<*>, bundle: Bundle? = null) {
    changePage(destinationPage, bundle, false)
}

fun Activity.changePage(destinationPage: Class<*>, finishActivity: Boolean) {
    changePage(destinationPage, null, finishActivity)
}

fun Activity.changePage(destinationPage: Class<*>, bundle: Bundle? = null, finishActivity: Boolean) {
    val intent = Intent(this, destinationPage)
    if (bundle != null)
        intent.putExtras(bundle)
    startActivity(intent)
    if (finishActivity) finish()
}

fun Activity.changePageClearPrevious(destinationPage: Class<*>,bundle: Bundle? = null) {
    val intent = Intent(this, destinationPage)
    if (bundle != null){
        val intent = Intent(this, destinationPage)
        intent.putExtras(bundle)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun String.versionToInt(): Int {
    return this.replace(".","").toInt()
}

fun Context.changePageClearPrevious(destinationPage: Class<*>,bundle: Bundle? = null) {
    val intent = Intent(this, destinationPage)
    if (bundle != null){
        val intent = Intent(this, destinationPage)
        intent.putExtras(bundle)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun Activity.changePageForResult(destinationPage: Class<*>, requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, destinationPage)
    if (bundle != null)
        intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}

fun Fragment.changePageForResult(destinationPage: Class<*>, requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(activity, destinationPage)
    if (bundle != null)
        intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}


fun Activity.changeRootPage(destinationPage: Class<*>) {
    val intent = Intent(this, destinationPage)
    startActivity(intent)
    finishAffinity()
}

fun SpannableString.withClickableSpan(stringStart:Int,stringEnd:Int, onClickListener: () -> Unit): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) = onClickListener.invoke()
    }
    //val clickablePartStart = indexOf(clickablePart)
    setSpan(clickableSpan,
        stringStart,
        stringEnd,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return this
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

fun AppCompatActivity.getString(name: String): String {
    return resources.getString(resources.getIdentifier(name, "string", packageName))
}

fun String.md5Encrypt(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun currencyFormatter(price: Long): String {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.ITALY) as DecimalFormat
    decimalFormat.applyPattern("###,###.##")
    return decimalFormat.format(price)
}

fun Long.currencyFormatterLong(): String {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.ITALY) as DecimalFormat
    decimalFormat.applyPattern("###,###.##")
    return decimalFormat.format(this)
}
fun Long.pointsFormatterLong(): String {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
    decimalFormat.applyPattern("###,###.##")
    return decimalFormat.format(this)
}

fun Long.pointFormatterLong(): String {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.ITALY) as DecimalFormat
    decimalFormat.applyPattern("###,###.##")
    return decimalFormat.format(this)
}


fun EditText.showPassword(){
    transformationMethod = HideReturnsTransformationMethod.getInstance()
    setSelection(this.text.length)
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timeZone: TimeZone = TimeZone.getTimeZone("GMT+7")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.getFormattedDateString(): String {
    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.timeInMillis = this.time
    return if (neededTime[Calendar.YEAR] === nowTime[Calendar.YEAR]) {
        if (neededTime[Calendar.MONTH] === nowTime[Calendar.MONTH]) {
            if (neededTime[Calendar.DATE] - nowTime[Calendar.DATE] === 1) { //here return like "Tomorrow at 12:00"
                "Tomorrow - " + DateFormat.format("dd MMMM yyyy", neededTime)
            } else if (nowTime[Calendar.DATE] === neededTime[Calendar.DATE]) { //here return like "Today at 12:00"
                "Today - " + DateFormat.format("dd MMMM yyyy", neededTime)
            } else if (nowTime[Calendar.DATE] - neededTime[Calendar.DATE] === 1) { //here return like "Yesterday at 12:00"
                "Yesterday - " + DateFormat.format("dd MMMM yyyy", neededTime)
            } else { //here return like "May 31, 12:00"
                DateFormat.format("EEEE - dd MMMM yyyy", neededTime).toString()
            }
        } else { //here return like "May 31, 12:00"
            DateFormat.format("EEEE - dd MMMM yyyy", neededTime).toString()
        }
    } else { //here return like "May 31 2010, 12:00" - it's a different year we need to show it
        DateFormat.format("EEEE - dd MMMM yyyy", neededTime).toString()
    }
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}
fun String.digitsOnly(): String{
    val regex = Regex("[^0-9]")
    return regex.replace(this, "")
}

fun EditText.hidePassword(){
    transformationMethod = PasswordTransformationMethod.getInstance()
    setSelection(this.text.length)
}

fun View.toggleVisibility() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}

fun View.toggleVisibility(isExpand:Boolean) {
    if (isExpand) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

fun View.toggleVisibeInvisible(isExpand:Boolean) {
    if (isExpand) {
        visibility = View.VISIBLE
    } else {
        visibility = View.INVISIBLE
    }
}

fun ImageView.toggleImage(isExpand:Boolean, trueImage: Int, falseImage: Int) {
    if (isExpand) {
        setImageResource(trueImage)
    } else {
        setImageResource(falseImage)
    }
}

fun View.toggleRotation(isExpand:Boolean) {

    if(isExpand){
        animate().rotation(0f).start()
    }else{
        animate().rotation(-180f).start()
    }
}

fun View.blinkAnimation() {
    val anim: Animation = AlphaAnimation(0.0f, 1.0f)
    anim.duration = 1000 //You can manage the blinking time with this parameter
    anim.repeatMode = Animation.REVERSE
    startAnimation(anim)
}

fun TextView.showAsHtml(source: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        text = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        text = Html.fromHtml(source)
    }
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}

fun String.firstLastNameGenerator(): Pair<String, String>{
    val firstSpaceIndex = this.indexOf(" ")
    val firstname = this.substring(0, firstSpaceIndex).trim()
    val lastname = this.substring(firstSpaceIndex, this.length).trim()
    return Pair (firstname, lastname)
}

fun String.orderStatusConverter(): Pair<String, Int> {

    var statusString = " - "
    var statusColosRes = R.color.dusk_blue
    when {
        this.equals("SUCCESS") -> {
            statusString = "Sukses"
            statusColosRes = R.color.dusk_blue
        }
        this.equals("PROCESSING") -> {
            statusString = "Sedang diproses"
            statusColosRes = R.color.gold
        }
        this.equals("FAILED") -> {
            statusString = "Gagal"
            statusColosRes = R.color.pinkish_red
        }
    }
    return Pair (statusString, statusColosRes)
}


var TextView.drawableEnd: Drawable?
    get() = compoundDrawablesRelative.get(2)
    set(value) = setDrawables(end = value)



fun TextView.setDrawables(
    start: Drawable? = null,
    top: Drawable? = null,
    end: Drawable? = null,
    bottom: Drawable? = null
) = setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)