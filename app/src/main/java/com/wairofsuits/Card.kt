package com.warofsuits

data class Card(
    val card: Int,
    val suit: Int,
    var suitPower: Int= 0,
    var onwer: Int = 0,
    var haveOnwer: Boolean = false,
    var regularPile: Boolean = true
)

data class Suit(
    var suit: Int,
    var power: Int = 0
)

const val CARD_2 = 0
const val CARD_3 = 1
const val CARD_4 = 2
const val CARD_5 = 3
const val CARD_6 = 4
const val CARD_7 = 5
const val CARD_8 = 6
const val CARD_9 = 7
const val CARD_10 = 8
const val CARD_J = 9
const val CARD_Q = 10
const val CARD_K = 11
const val CARD_A = 12

const val SUIT_CLUBS = 0
const val CARD_DIAMONDS = 1
const val CARD_HEARTS = 2
const val CARD_SPADES = 3
