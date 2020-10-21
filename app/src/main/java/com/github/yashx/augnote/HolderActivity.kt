package com.github.yashx.augnote

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.github.yashx.augnote.databinding.ActivityHolderBinding
import com.github.yashx.augnote.utils.Constants
import com.github.yashx.augnote.utils.PrefHelper
import org.koin.android.ext.android.inject


class HolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolderBinding
    private val prefHelper: PrefHelper by inject()
    private var _backButtonVisible: Boolean = false
    private var _hamburgerButtonVisible: Boolean = false
    private val navController by lazy { (supportFragmentManager.findFragmentByTag("holder") as NavHostFragment).navController }

    var backButtonVisible: Boolean
        set(value) {
            _hamburgerButtonVisible = !value
            if (value)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            _backButtonVisible = value
        }
        get() = _backButtonVisible

    var hamburgerButtonVisible: Boolean
        set(value) {
            _backButtonVisible = !value
            if (value)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_ham)
            _hamburgerButtonVisible = value
        }
        get() = _hamburgerButtonVisible

    var toolbarTitle: String = "Home"
        set(value) {
            binding.holderActivityToolbar.title = value
            field = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHolderBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            setSupportActionBar(bottomAppBar)
        }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (backButtonVisible)
                onBackPressed()
            else if (hamburgerButtonVisible) {
                println("HERE")
                PopupMenu(this, binding.anchor).apply {
                    inflate(R.menu.menu_holder_activity_bottom_sheet)
                    if (prefHelper.isPro)
                        menu.findItem(R.id.goPro).isVisible = false
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.augnoteAbout -> navController.navigate(NavGraphDirections.actionGlobalAboutLibraryDestinationFragment())
                            R.id.changeTheme -> {
                                if (prefHelper.isPro)
                                    navController.navigate(NavGraphDirections.actionGlobalThemeOptionsDialogFragment())
                                else
                                    Toast.makeText(this@HolderActivity, "Pro Feature",Toast.LENGTH_SHORT).show()
//                                    pro
                            }
//                            R.id.goPro -> goPro
//                            R.id.shareApp ->
//                            R.id.rateApp ->
                            else -> Toast.makeText(this@HolderActivity, it.title, Toast.LENGTH_SHORT).show()

                        }
                        true
                    }
                    setOnDismissListener {
                        binding.overlay.isVisible = false
                    }
                    binding.overlay.isVisible = true
                    show()
                }
            }
            return true
        }
        return false
    }
}