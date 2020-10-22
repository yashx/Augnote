package com.github.yashx.augnote

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.github.yashx.augnote.databinding.ActivityHolderBinding
import com.github.yashx.augnote.utils.Constants
import com.github.yashx.augnote.utils.PrefHelper
import com.vojtkovszky.billinghelper.BillingEvent
import com.vojtkovszky.billinghelper.BillingHelper
import com.vojtkovszky.billinghelper.BillingListener
import org.koin.android.ext.android.inject
import timber.log.Timber


class HolderActivity : AppCompatActivity(), BillingListener {

    private lateinit var binding: ActivityHolderBinding
    private val prefHelper: PrefHelper by inject()
    private var _backButtonVisible: Boolean = false
    private var _hamburgerButtonVisible: Boolean = false
    private lateinit var context: Context
    private val navController by lazy { (supportFragmentManager.findFragmentByTag("holder") as NavHostFragment).navController }

    private lateinit var billingHelper: BillingHelper

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

        context = this

        billingHelper = BillingHelper(context, listOf(Constants.IN_APP_PRODUCT_ID), billingListener = this)

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
                PopupMenu(context, binding.anchor).apply {
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
                                    startPurchaseFlow()
                            }
                            R.id.goPro -> startPurchaseFlow()
                            R.id.shareApp -> {
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                                    val message =
                                        """
                                            I found an App to make better Handwritten Notes.
                                            
                                            Try it out
                                            http://play.google.com/store/apps/details?id=${context.packageName}
                                        """.trimIndent()
                                    putExtra(Intent.EXTRA_TEXT, message)
                                    startActivity(Intent.createChooser(this, getString(R.string.share_app)))
                                }
                            }
                            R.id.rateApp -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${context.packageName}")))
                            else -> Toast.makeText(context, it.title, Toast.LENGTH_SHORT).show()

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

    private fun startPurchaseFlow() {
        if (prefHelper.isPro)
            return
        billingHelper.launchPurchaseFlow(this, Constants.IN_APP_PRODUCT_ID)
    }

    override fun onDestroy() {
        billingHelper.endBillingClientConnection()
        super.onDestroy()
    }

    override fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?) {
        Timber.e("onBillingEvent:$message ")
        when (event) {
            BillingEvent.BILLING_CONNECTION_FAILED, BillingEvent.PURCHASE_FAILED -> toast(R.string.try_again)
            BillingEvent.PURCHASE_COMPLETE -> {
                if (billingHelper.isPurchased(Constants.IN_APP_PRODUCT_ID))
                    prefHelper.isPro = true
                toast(R.string.purchase_success)
            }
            BillingEvent.QUERY_OWNED_PURCHASES_COMPLETE -> {
                Timber.e("Owned: ${billingHelper.isPurchased(Constants.IN_APP_PRODUCT_ID)}")
                prefHelper.isPro = billingHelper.isPurchased(Constants.IN_APP_PRODUCT_ID)
            }
        }
    }

    private fun toast(strID: Int) {
        Toast.makeText(context, strID, Toast.LENGTH_SHORT).show()
    }
}