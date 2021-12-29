package com.ezz.colors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class GameViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setup() {
        gameViewModel = GameViewModel()
    }

    @Test
    fun `initialize takes a random color and name then puts it to LiveData`(){
        val oldPair = gameViewModel.queryMLD.getOrAwaitValueTest()
        gameViewModel.initialize()
        val newPair = gameViewModel.queryMLD.getOrAwaitValueTest()
        assertThat(newPair).isNotEqualTo(oldPair)
    }

    @Test
    fun `initialize makes choices list that contains query color and random colors`(){
        val oldList = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        gameViewModel.initialize()
        val newList = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        assertThat(newList).isNotEqualTo(oldList)
    }

    @Test
    fun `choices list contains query color`(){
        gameViewModel.initialize()
        val colorName = gameViewModel.queryMLD.getOrAwaitValueTest().first
        val newList = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        val realColor = gameViewModel.colorsList[gameViewModel.colorNamesList.indexOf(colorName)].first
        assertThat(newList).contains(realColor)
    }

    @Test
    fun `choices list contains color of query text`(){
        gameViewModel.initialize()
        val color = gameViewModel.queryMLD.getOrAwaitValueTest().second
        val newList = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        assertThat(newList).contains(color)
    }

    @Test
    fun `if chosen color equals query color, score increases by 1`(){
        val oldScore = gameViewModel.scoreMLD.getOrAwaitValueTest()
        gameViewModel.queryMLD.value = Pair(R.string.red, R.color.blue_a400)
        gameViewModel.choose(-59580)

        val score = gameViewModel.scoreMLD.getOrAwaitValueTest()
        assertThat(score).isEqualTo(oldScore + 1)
    }

    @Test
    fun `if chosen color not equals query color, score resets to zero`(){
        gameViewModel.scoreMLD.value = 12
        gameViewModel.queryMLD.value = Pair(R.string.red, R.color.blue_a400)
        gameViewModel.choose(R.color.purple_a400)

        val score = gameViewModel.scoreMLD.getOrAwaitValueTest()
        assertThat(score).isEqualTo(0)
    }

    @Test
    fun `if chosen color equals query color, new query initialized`(){
        gameViewModel.queryMLD.value = Pair(R.string.red, R.color.blue_a400)
        val oldQuery = gameViewModel.queryMLD.getOrAwaitValueTest()
        gameViewModel.choose(R.color.blue_a400)
        val newQuery = gameViewModel.queryMLD.getOrAwaitValueTest()
        assertThat(newQuery).isNotEqualTo(oldQuery)
    }

    @Test
    fun `if chosen color equals query color, new choices initialized`(){
        gameViewModel.queryMLD.value = Pair(R.string.red, R.color.blue_a400)
        val oldChoices = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        gameViewModel.choose(R.color.blue_a400)
        val newChoices = gameViewModel.choicesListMLD.getOrAwaitValueTest()
        assertThat(newChoices).isNotEqualTo(oldChoices)
    }

    @Test
    fun `if score is greater than high score, set new high score`(){
        val newScore = 5
        gameViewModel.scoreMLD.value = newScore
        gameViewModel.highestMLD.value = 3
        gameViewModel.setHighestScore(newScore)
        val newHighest = gameViewModel.highestMLD.getOrAwaitValueTest()
        assertThat(newHighest).isEqualTo(newScore)
    }

    @Test
    fun `if score is greater than 10 , size will be 6`(){
        gameViewModel.increaseSize(11)
        val newSize = gameViewModel.sizeMLD.getOrAwaitValueTest()
        assertThat(newSize).isEqualTo(6)
    }
    @Test
    fun `if score is greater than 25 , size will be 9`(){
        gameViewModel.increaseSize(26)
        val newSize = gameViewModel.sizeMLD.getOrAwaitValueTest()
        assertThat(newSize).isEqualTo(9)
    }

    @Test
    fun `pressing reset , resets score to 0`(){
        gameViewModel.scoreMLD.value = 15
        gameViewModel.reset(true)
        val score = gameViewModel.scoreMLD.getOrAwaitValueTest()
        assertThat(score).isEqualTo(0)
    }
    @Test
    fun `pressing reset , resets size to 4`(){
        gameViewModel.sizeMLD.value = 6
        gameViewModel.reset(true)
        val size = gameViewModel.sizeMLD.getOrAwaitValueTest()
        assertThat(size).isEqualTo(4)
    }


}