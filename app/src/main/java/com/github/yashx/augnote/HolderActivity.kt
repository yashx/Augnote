package com.github.yashx.augnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.yashx.augnote.databinding.ActivityHolderBinding
import org.koin.android.ext.android.get

class HolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(ActivityHolderBinding.inflate(layoutInflater)) {
            setContentView(root)
            val fragment = supportFragmentManager.findFragmentByTag("holder") as NavHostFragment
            toolbar.setupWithNavController(fragment.navController, AppBarConfiguration(fragment.navController.graph))
            setSupportActionBar(toolbar)
        }

//        val database:Database = get()
//        database.augnoteQueries.insertFolder(0,-1,"root")
    }
}