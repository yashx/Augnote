package com.github.yashx.augnote.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.github.yashx.augnote.R

class PrefHelper(context: Context) {

    private val pref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var themeMode: String
        get() = pref.getString(Constants.PREF_THEME_OPTION, Constants.LIGHT_THEME_OPTION)!!
        set(value) {
            pref.edit {
                when (value) {
                    Constants.LIGHT_THEME_OPTION, Constants.DARK_THEME_OPTION, Constants.SYSTEM_THEME_OPTION -> putString(Constants.PREF_THEME_OPTION, value)
                }
            }
        }

    var isPro: Boolean
        get() = pref.getBoolean(Constants.PREF_IS_PRO, false)
        set(value) {
            pref.edit {
                if(!value){
                    themeMode = Constants.LIGHT_THEME_OPTION
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                putBoolean(Constants.PREF_IS_PRO, value)
            }
        }
}