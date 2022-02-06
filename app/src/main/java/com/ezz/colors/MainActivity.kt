package com.ezz.colors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            Utils.playSound(this, 1)
        }

        b.aboutButton.setOnClickListener {
            showAboutDeveloper()
            Utils.playSound(this, 1)
        }
    }

    /**
     * shows information about developer in alert dialog
     */
    private fun showAboutDeveloper() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle(getString(R.string.about))
            .setMessage(getString(R.string.info))
            .setNegativeButton("Dismiss", null)
            .show()
    }
}