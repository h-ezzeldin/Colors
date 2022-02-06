package com.ezz.colors

import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore


/**
 * utils class for common methods
 */
class Utils {


    companion object {
        private var mediaPlayer: MediaPlayer? = null

        /**
         * creates a mediaPlayer sound and starts it
         *
         * @param context activity which called the method
         * @param type given sound number to play
         */
        fun playSound(context: Context, type: Int) {
            val sound = when (type) {
                1 -> R.raw.button_click // button click sound
                2 -> R.raw.correct      // correct choice sound
                3 -> R.raw.incorrect    // incorrect choice sound
                4 -> R.raw.level_up     // level up sound
                else -> R.raw.button_click
            }

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, sound)
            mediaPlayer?.start()
        }


        /**
         * releases created mediaPlayer file
         */
        fun releasePlayer(){
            mediaPlayer?.release()
            mediaPlayer = null
        }

    }

}