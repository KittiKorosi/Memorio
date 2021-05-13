package com.example.memorio.models

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import com.example.memorio.MainActivity
import com.example.memorio.utils.DEFAULT_ICONS
import java.util.*

class MemoryGame (private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var indexOfSingleSelectedCard: Int? = null
    private var numCardFlips = 0


    init {
        val chosenImages: List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages: List<Int> = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }


    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card : MemoryCard = cards[position]
        // three cases
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        }else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFacedUp = !card.isFacedUp


        if (card.isFacedUp) {

                var text = "hello"
            MainActivity().speak("text")
        }
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card: MemoryCard in cards) {
            if (!card.isMatched) {
                card.isFacedUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFacedUp(position: Int): Boolean {
        return cards[position].isFacedUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2

    }
}