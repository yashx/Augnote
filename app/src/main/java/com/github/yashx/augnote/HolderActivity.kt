package com.github.yashx.augnote

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.yashx.augnote.databinding.ActivityHolderBinding


class HolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolderBinding
    var backButtonVisible: Boolean = true
        set(value) {
            supportActionBar?.setDisplayHomeAsUpEnabled(value)
            field = value
        }

    var toolbarTitle:String = "Home"
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
            onBackPressed()
        }
        return false
    }
}