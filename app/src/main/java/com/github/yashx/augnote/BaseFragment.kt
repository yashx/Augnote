package com.github.yashx.augnote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    private val holderActivity
        get() = requireActivity() as HolderActivity

    abstract fun shouldShowBackButton(): Boolean
    abstract fun fragmentTitle(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holderActivity.backButtonVisible = shouldShowBackButton()
        holderActivity.toolbarTitle = fragmentTitle()
    }
}