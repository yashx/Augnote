package com.github.yashx.augnote

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.yashx.augnote.databinding.ActivityHolderBinding

class HolderActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(ActivityHolderBinding.inflate(layoutInflater)) {
            setContentView(root)
            val fragment = supportFragmentManager.findFragmentByTag("holder") as NavHostFragment
            navController = fragment.navController
            bottomAppBar.setupWithNavController(fragment.navController, AppBarConfiguration(fragment.navController.graph))
            setSupportActionBar(bottomAppBar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            navController.popBackStack()
        }
        return false
    }
}