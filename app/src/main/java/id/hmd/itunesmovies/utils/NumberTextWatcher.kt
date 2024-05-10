package id.hmd.itunesmovies.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


/**
 * Created by hmdrrhmn on 2019-10-09 at 14:14.
 */
class NumberTextWatcher : TextWatcher {
    private var editText: EditText? = null
    private var enableZeroFirstChar: Boolean = true

    constructor(editText: EditText) {
        this.editText = editText
    }

    constructor(editText: EditText, enableZeroFirstChar: Boolean) {
        this.editText = editText
        this.enableZeroFirstChar = enableZeroFirstChar
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        try {
            editText!!.removeTextChangedListener(this)
            val value = editText!!.text.toString()

            if (value != "") {
                if (value.startsWith(".")) {
                    editText!!.setText("0.")
                }
                if (!enableZeroFirstChar && value.startsWith("0") && !value.startsWith("0.")) {
                    editText!!.setText("")
                }

                if (enableZeroFirstChar && value.startsWith("0")) {
                    editText!!.setText("0")
                }

                if (value != ""){
                    val str = editText!!.text.toString().digitsOnly()
                    val strFormatted = str.toLong().currencyFormatterLong()
                    editText!!.setText(strFormatted)
                }
                editText!!.setSelection(editText!!.text.toString().length)
            }
            editText!!.addTextChangedListener(this)
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText!!.addTextChangedListener(this)
        }

    }
}