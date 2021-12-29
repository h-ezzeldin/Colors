package com.ezz.colors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.ezz.colors.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * this game developed by Hussein Ezz
 * mail: hezzeldin1@gmail.com
 * @author Hussein Ezz
 */
class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.startButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        b.aboutButton.setOnClickListener { showAboutDeveloper() }

    }

    /**
     * shows information about developer in alert dialog
     */
    private fun showAboutDeveloper() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setBackground(AppCompatResources.getDrawable(this@MainActivity, R.color.white))
            .setTitle(getString(R.string.about))
            .setMessage(getString(R.string.info))
            .setNegativeButton("Dismiss", null)
            .show()
    }
}