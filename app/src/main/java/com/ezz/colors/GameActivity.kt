package com.ezz.colors

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.ezz.colors.databinding.ActivityGameBinding
import com.google.android.material.card.MaterialCardView


class GameActivity : AppCompatActivity() {
    private lateinit var b: ActivityGameBinding
    private lateinit var gameViewModel: GameViewModel
    private var highestScore = 0
    private var lastLevel = 4
    private var init = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityGameBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.score = 0
        b.highest = 0

        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        // gets stored score from shared preferences
        highestScore = getHighScore()
        gameViewModel.highestMLD.value = highestScore

        gameViewModel.scoreMLD.observe(this) {
            if (!init) {
                animateScores(b.scoreText, it > b.score!!.toInt())
                Utils.playSound(this, if (it > b.score!!.toInt()) 2 else 3)
            }
            init = false
            b.score = it
        }

        gameViewModel.highestMLD.observe(this) {
            if (it > highestScore) setHighScore(it)
            if (!init) {
                animateScores(b.highScoreText, it > b.highest!!.toInt())
            }
            highestScore = it
            b.highest = it
        }

        gameViewModel.queryMLD.observe(this) {
            b.color = it.second
            b.colorName = it.first
        }

        gameViewModel.choicesListMLD.observe(this) { list ->
            setColorsGrid(list)
        }

        b.backButton.setOnClickListener {

            onBackPressed()
            Utils.playSound(this, 1)
        }

        b.resetButton.setOnClickListener {
            init = true
            gameViewModel.reset(true)
            Utils.playSound(this, 1)
        }
    }

    /**
     * adds color cards to GridLayout
     * adds click listeners for each card
     * @param list list of colors
     */
    private fun setColorsGrid(list: List<Int>) {
        val gridLayout = b.playGridLayout
        gridLayout.removeAllViews()
        gridLayout.columnCount = if (list.size <= 6) 2 else 3
        gridLayout.rowCount = if (list.size <= 4) 2 else 3

        list.forEach {
            val card = MaterialCardView(this)
            card.contentDescription = getString(R.string.color_card)
            gridLayout.addView(card)
            setCardStyle(card)
            card.setCardBackgroundColor(getColor(it))
            animateCards(card)
        }

        gridLayout.children.forEach { card ->
            card.setOnClickListener {
                gameViewModel.choose(
                    (card as MaterialCardView).cardBackgroundColor.defaultColor
                )
            }
        }

        if (list.size > lastLevel) {
            Utils.playSound(this, 4)
            lastLevel = list.size
        }
    }

    /**
     * a helper function to add attributes to color card
     * adds columnWeight, rowWeight and margin
     * @param view cardView
     */
    private fun setCardStyle(view: View) {
        val layoutParams = (view.layoutParams as GridLayout.LayoutParams)
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 4F,
            resources.displayMetrics
        ).toInt()
        layoutParams.setMargins(px, px, px, px)
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
    }

    /**
     * gets stored score from shared preferences
     * @return score
     */
    private fun getHighScore(): Int {
        return getPreferences(Context.MODE_PRIVATE).getInt("high", 0)
    }

    /**
     * set the new high score to shared preferences
     * @param score new high score
     */
    private fun setHighScore(score: Int) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("high", score).apply()

    }

    /**
     * animates scores by changing scale and color
     * @param view given view
     * @param wins checks if wins or losses
     */
    private fun animateScores(view: View, wins: Boolean) {
        val color = if (wins) R.color.green_a400 else R.color.red_a400

        (view as TextView).setTextColor(getColor(color))

        view.animate()
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(200L)
            .setInterpolator(LinearInterpolator())
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200L)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction {
                        (view).setTextColor(getColor(R.color.white))
                    }
            }
    }

    /**
     * animates scores by changing scale and rotation
     * @param view given view
     */
    private fun animateCards(view: View) {
        view.scaleX = 0f
        view.scaleY = 0f

        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .rotation(360f)
            .setDuration(300L).interpolator = LinearInterpolator()

    }

    override fun onStop() {
        super.onStop()
        Utils.releasePlayer()
    }

}