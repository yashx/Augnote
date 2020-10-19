package com.github.yashx.augnote.utils

import android.content.Context
import androidx.core.content.edit
import com.github.yashx.augnote.R

class PrefHelper(context: Context) {
    private val pref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    var isInDarkMode: Boolean
        get() = pref.getBoolean(Constants.PREF_DARK_MODE, false)
        set(value) {
            pref.edit {
                putBoolean(Constants.PREF_DARK_MODE, value)
            }
        }
}