package com.wairofsuits

import androidx.lifecycle.MutableLiveData
import com.warofsuits.Card
import com.warofsuits.Suit
import kotlin.random.Random

class CardViewModel {

    private val cards = MutableLiveData<List<Card>>()
    private val suits = MutableLiveData<List<Suit>>()

    fun inicialGame() {
        creatSuits()
        givePowerSuits()
        createCards()
        giveCardsPlayers()
    }

    fun getCards(): List<Card> {
        return cards.value!!
    }

    fun getSuit(): List<Suit> {
        return suits.value!!
    }


    private fun createCards() {
        val card = mutableListOf<Card>()
        for (i in 0..3) {
            for (j in 0..12) {
                val suitPower = getSuit().filter { it.suit == i }[0].power
                card.add(
                    Card(
                        card = j,
                        suit = i,
                        suitPower = suitPower
                    )
                )
            }
        }
        cards.value = card
    }

    private fun creatSuits() {
        val suit = mutableListOf<Suit>()
        for (i in 0..3) {
            suit.add(Suit(i))
        }
        suits.value = suit
    }

    private fun givePowerSuits() {
        do {
            val powerRandom = Random.nextInt(from = 1, until = 5)
            val getRandomSuit = getSuit().random()
            val indexGetRandomSuit = getSuit().indexOf(getRandomSuit)
            if (getRandomSuit.power == 0) {
                val checklist = getSuit().filter { it.power == powerRandom }
                if (checklist.isEmpty()) {
                    getSuit()[indexGetRandomSuit].power = powerRandom
                }
            }

        } while (getSuit().any { it.power == 0 })
    }

    private fun totalOnwerCards(): Int {
        return getCards().filter { it.haveOnwer }.size
    }

    private fun totalCardsPlayer1(): Int {
        return getCardsPlayer1().size
    }

    fun totalCardsPlayer2(): Int {
        return getCardsPlayer2().size
    }

    private fun giveCardsPlayers() {
        val cardToEachPlayer = getCards().size / 2
        do {
            for (i in getCards().indices) {
                val getRandomCardOnCards = getCards().random()
                val indexGetRandomCardOnCards = getCards().indexOf(getRandomCardOnCards)
                if (!getRandomCardOnCards.haveOnwer) {
                    if (totalCardsPlayer1() != cardToEachPlayer) {
                        getCards()[indexGetRandomCardOnCards].haveOnwer = true
                        getCards()[indexGetRandomCardOnCards].onwer = 1
                    } else {
                        getCards()[indexGetRandomCardOnCards].haveOnwer = true
                        getCards()[indexGetRandomCardOnCards].onwer = 2
                    }
                }
            }
        } while (totalOnwerCards() != getCards().size)
    }

    fun getCardsPlayer1(): List<Card> {
        return getCards().filter { it.onwer == 1 }
    }

    fun getCardsPlayer2(): List<Card> {
        return getCards().filter { it.onwer == 2 }
    }

    fun getRandomCardPlayer1(): Card {
        val filter = getCardsPlayer1().filter { it.regularPile }
        return filter.random()
    }

    fun getRandomCardPlayer2(): Card {
        val filter = getCardsPlayer2().filter { it.regularPile }
        return filter.random()
    }

    fun whoWin(card1: Card, card2: Card): Int {
        var returnV = 0
        if (card1.card > card2.card) {
            returnV = 1
        } else if (card2.card > card1.card) {
            returnV = 2
        } else if (card1.card == card2.card) {
            returnV = if (card1.suitPower > card2.suitPower)
                1
            else 2
        }
        return returnV
    }

    fun doRoad(card1: Card, card2: Card) {
        if (whoWin(card1, card2) == 1) {
            putCardDiscardPile(card1)
            putCardDiscardPile(card2)
            putCardToWinner(card1, card2, 1)
        } else if (whoWin(card1, card2) == 2) {
            putCardDiscardPile(card1)
            putCardDiscardPile(card2)
            putCardToWinner(card1, card2, 2)
        }

    }

    fun getRoundStatus(integer: Boolean = false): String {
        var returnV = ""
        val cardsDiscardsPlayer1 = getCardsPlayer1().filter { !it.regularPile }.size
        val cardsDiscardsPlayer2 = getCardsPlayer2().filter { !it.regularPile }.size
        val resultInt = ((cardsDiscardsPlayer1 + cardsDiscardsPlayer2) / 2) + 1

        returnV = if (integer) resultInt.toString()
        else "round\n$resultInt"

        return returnV
    }

    fun checkEndGame(): Boolean {
        val totalRegularCardsPlayer1 = getCardsPlayer1().filter { it.regularPile }.size
        val totalRegularCardsPlayer2 = getCardsPlayer2().filter { it.regularPile }.size
        val sumRegularCards = totalRegularCardsPlayer1 + totalRegularCardsPlayer2

        return sumRegularCards <= 0
    }
    fun checkGamerWinner(integer: Boolean = false): String {
        val cardsDiscardsPlayer1 = getCardsPlayer1().filter { !it.regularPile }.size
        val cardsDiscardsPlayer2 = getCardsPlayer2().filter { !it.regularPile }.size
        var returnV=""
        returnV = if(integer){
            if(cardsDiscardsPlayer1>cardsDiscardsPlayer2){
                "1"
            }else if(cardsDiscardsPlayer2>cardsDiscardsPlayer2){
                "2"
            }else "0"
        }else{
            if(cardsDiscardsPlayer1>cardsDiscardsPlayer2){
                "Player 1\nWIN THE GAME"
            }else if(cardsDiscardsPlayer2>cardsDiscardsPlayer1){
                "Player 2\nWIN THE GAME"
            }else if(cardsDiscardsPlayer1==cardsDiscardsPlayer2){
                "TIE GAME"
            }else  {
                "UNKNOWN"
            }
        }


        return returnV
    }

    private fun putCardDiscardPile(card: Card) {
        val indexCard = getCards().indexOf(card)
        getCards()[indexCard].regularPile = false
    }

    private fun putCardToWinner(card1: Card, card2: Card, winner: Int) {
        val indexCard1 = getCards().indexOf(card1)
        val indexCard2 = getCards().indexOf(card2)
        getCards()[indexCard1].onwer = winner
        getCards()[indexCard2].onwer = winner
    }

}