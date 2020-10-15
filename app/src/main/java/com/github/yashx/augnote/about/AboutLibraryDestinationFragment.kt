package com.github.yashx.augnote.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.yashx.augnote.HolderActivity
import com.github.yashx.augnote.R
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment

class AboutLibraryDestinationFragment : LibsSupportFragment() {

    private val holderActivity
        get() = requireActivity() as HolderActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holderActivity.backButtonVisible = true
        holderActivity.toolbarTitle = "About"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        with(Bundle()) {
            // customize LibsBuilder object as needed
            putSerializable(
                "data",
                LibsBuilder().apply {
                    withAboutDescription(getString(R.string.tag_line))
                    withLicenseShown(true)
                })
            return libsFragmentCompat.onCreateView(inflater.context, inflater, container, savedInstanceState, this)
        }
    }
}