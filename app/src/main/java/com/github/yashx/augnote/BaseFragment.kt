package com.github.yashx.augnote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.yashx.augnote.holder.HolderActivity

abstract class BaseFragment : Fragment() {
    private val holderActivity
        get() = requireActivity() as HolderActivity

    /**
     * Should show Back Button instead of Hamburger icon, Behaviour is managed by activity
     *
     * @return true to show Back Icon, false to show Hamburger Icon
     */
    abstract fun shouldShowBackButton(): Boolean

    abstract fun fragmentTitle(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(shouldShowBackButton())
            holderActivity.backButtonVisible = true
        else
            holderActivity.hamburgerButtonVisible = true
        holderActivity.toolbarTitle = fragmentTitle()
    }
}