package com.ezz.colors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var colorsList = mutableListOf(
        R.color.red_a400 to -59580,
        R.color.pink_a700 to -3862174,
        R.color.purple_a400 to -2817799,
        R.color.blue_a400 to -14059009,
        R.color.cyan_a400 to -16718337,
        R.color.green_a400 to -16718218,
        R.color.yellow_a400 to -5632,
        R.color.orange_a400 to -28416,
        R.color.brown_500 to -8825528,
        R.color.white to -1
    )

    val colorNamesList = mutableListOf(
        R.string.red,
        R.string.pink,
        R.string.purple,
        R.string.blue,
        R.string.cyan,
        R.string.green,
        R.string.yellow,
        R.string.orange,
        R.string.brown,
        R.string.white
    )

    val sizeMLD = MutableLiveData<Int>().apply { value = 4 }
    val scoreMLD = MutableLiveData<Int>().apply { value = 0 }
    val highestMLD = MutableLiveData<Int>().apply { value = 0 }
    val queryMLD = MutableLiveData<Pair<Int, Int>>().apply { value = Pair(0, 0) }
    val choicesListMLD = MutableLiveData<List<Int>>().apply { value = listOf() }

    init {
        initialize()
    }

    /**
     * generates two different random colors
     * then puts them to query pair
     */
    fun initialize() {
        var color = colorsList.random().first
        val colorName = colorNamesList.random()
        val colorOfName = colorsList[colorNamesList.indexOf(colorName)].first
        while (colorOfName == color) color = colorsList.random().first
        queryMLD.value = Pair(colorName, color)
        makeChoices(color, colorOfName, sizeMLD.value!!)
    }

    /**
     * generates the list of color choices
     * @param color text color
     * @param colorOfName color that described by text
     * @param size size of new ChoicesList
     */
    private fun makeChoices(color: Int, colorOfName: Int, size: Int) {
        val newChoicesList =
            mutableListOf<Int>()//colorsList.shuffled().toMutableList().subList(0, size-2)
        newChoicesList.add(colorOfName)
        newChoicesList.add(color)
        while (newChoicesList.size < size) {
            val newColor = colorsList.random().first
            if (!newChoicesList.contains(newColor)) {
                newChoicesList.add(newColor)
            }
        }
        choicesListMLD.value = newChoicesList.shuffled()
    }

    /**
     * takes chosen color by user then compares it with the query color
     * if is equals increases score
     * else resets score to zero
     * @param color chosen color
     */
    fun choose(color: Int) {
        val queryColor = colorsList[colorNamesList.indexOf(queryMLD.value!!.first)].second
        if (color == queryColor) {
            scoreMLD.value = scoreMLD.value!! + 1
            increaseSize(scoreMLD.value!!)
            if (scoreMLD.value!! > highestMLD.value!!) setHighestScore(scoreMLD.value!!)
        } else {
            reset(false)
        }
        initialize()
    }

    /**
     * sets new high score to high score live data
     * @param newScore new high score
     */
    fun setHighestScore(newScore: Int) {
        highestMLD.value = newScore
    }

    /**
     * increases size of choices list based on score
     * @param score current score
     */
    fun increaseSize(score: Int) {
        if (score in 11..25) sizeMLD.value = 6
        else if (score > 25) sizeMLD.value = 9
    }

    /**
     * resets score and size to defaults
     * @param fromButton checks if from reset button or from choosing
     */
    fun reset(fromButton: Boolean) {
        scoreMLD.value = 0
        sizeMLD.value = 4
        if (fromButton) initialize()
    }


}