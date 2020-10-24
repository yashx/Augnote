package com.github.yashx.augnote.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.yashx.augnote.holder.HolderActivity
import com.github.yashx.augnote.R
import com.github.yashx.augnote.utils.Constants
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsConfiguration
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment

class AboutLibraryDestinationFragment : LibsSupportFragment() {

    private val holderActivity
        get() = requireActivity() as HolderActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holderActivity.backButtonVisible = true
        holderActivity.toolbarTitle = getString(R.string.about)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        with(Bundle()) {
            // customize LibsBuilder object as needed
            putSerializable(
                "data",
                LibsBuilder().apply {
                    withAboutDescription(getString(R.string.tag_line))
                    withLicenseShown(true)
                    aboutAppSpecial1 = getString(R.string.contact)
                    withListener(object : LibsConfiguration.LibsListener {
                        override fun onExtraClicked(v: View, specialButton: Libs.SpecialButton) = when (specialButton) {
                            Libs.SpecialButton.SPECIAL1 -> {
                                with(Intent(Intent.ACTION_SENDTO)) {
                                    data = Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.DEVELOPER_EMAIL))
                                    putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT)
                                    putExtra(Intent.EXTRA_TEXT, Constants.EMAIL_BODY)
                                    startActivity(Intent.createChooser(this, getString(R.string.send_email)))
                                }
                                true
                            }
                            else -> false
                        }

                        override fun onIconClicked(v: View) {}
                        override fun onIconLongClicked(v: View) = false
                        override fun onLibraryAuthorClicked(v: View, library: Library) = false
                        override fun onLibraryAuthorLongClicked(v: View, library: Library) = false
                        override fun onLibraryBottomClicked(v: View, library: Library) = false
                        override fun onLibraryBottomLongClicked(v: View, library: Library) = false
                        override fun onLibraryContentClicked(v: View, library: Library) = false
                        override fun onLibraryContentLongClicked(v: View, library: Library) = false
                    })
                })
            return libsFragmentCompat.onCreateView(inflater.context, inflater, container, savedInstanceState, this)
        }
    }
}