package com.ttw.currencyexchange.utils

import android.app.Activity
import android.content.Context
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.ResponseBody

const val API_KEY = "dd15050d91ab7d6cfc97f88e698d3ae4"
const val BASE_URL = "http://api.currencylayer.com/"
const val LAST_FETCH_TIMESTAMP = "LAST_FETCH_TIMESTAMP"

fun getCurrentTimeStampInMillis(): Long {
    return System.currentTimeMillis() / 1000
}


inline fun <reified T> ResponseBody.parseError(): T? {
    val moshi = Moshi.Builder().build()
    val parser = moshi.adapter(T::class.java)
    val response = this.string()
    if (response != null)
        try {
            return parser.fromJson(response)
        } catch (e: JsonDataException) {
            e.printStackTrace()
        }
    return null
}

fun setPreferenceLong(context: Context, key: String, value: Long) {
    val sharedPreference = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putLong(key, value)
    editor.apply()
}

fun getPreferenceLong(context: Context, key: String): Long {
    val sharedPreference = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    return sharedPreference.getLong(key, 0)
}

fun AppCompatEditText.textInputAsFlow() = callbackFlow {
    val watcher: TextWatcher = doOnTextChanged { textInput: CharSequence?, _, _, _ ->
        this.trySend(textInput).isSuccess
    }
    awaitClose { this@textInputAsFlow.removeTextChangedListener(watcher) }
}

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken,
            0
        )
    }
}