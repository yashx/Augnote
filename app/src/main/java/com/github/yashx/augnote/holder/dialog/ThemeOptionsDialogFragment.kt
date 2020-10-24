package com.github.yashx.augnote.holder.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.DialogFragmentThemeOptionsBinding
import com.github.yashx.augnote.utils.Constants
import com.github.yashx.augnote.utils.PrefHelper
import org.koin.android.ext.android.inject

class ThemeOptionsDialogFragment : DialogFragment() {
    private val pref: PrefHelper by inject()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogFragmentThemeOptionsBinding.inflate(layoutInflater).apply {
            root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        binding.lightThemeOption.setOnClickListener {
            changeThemeMode(Constants.LIGHT_THEME_OPTION)
        }

        binding.darkThemeOption.setOnClickListener {
            changeThemeMode(Constants.DARK_THEME_OPTION)
        }

        binding.systemThemeOption.setOnClickListener {
            changeThemeMode(Constants.SYSTEM_THEME_OPTION)
        }

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setTitle(R.string.choose_theme)
            setView(binding.root)
            create()
        }
    }

    private fun changeThemeMode(mode:String){
        pref.themeMode = mode
        AppCompatDelegate.setDefaultNightMode(
            when (mode) {
                Constants.SYSTEM_THEME_OPTION -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                Constants.DARK_THEME_OPTION -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        this@ThemeOptionsDialogFragment.dismiss()
    }
}