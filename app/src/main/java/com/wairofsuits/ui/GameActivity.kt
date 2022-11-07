package com.wairofsuits.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.wairofsuits.*
import com.wairofsuits.viewModel.CardViewModel

class GameActivity : AppCompatActivity(){

    private val viewModel = CardViewModel()
    private var player1RoundCard: Card? = null
    private var player2RoundCard: Card? = null
    private lateinit var instructionsPlayer2: TextView
    private lateinit var cardViewPlayer2: CardView
    private lateinit var cardNumberPlayer2: TextView
    private lateinit var cardSuitPlayer2: ImageView
    private lateinit var totalDiscardPilePlayer2: TextView
    private lateinit var totalRegularPilePlayer2: TextView
    private lateinit var textDiscardPilePlayer2: TextView
    private lateinit var instructionsPlayer1: TextView
    private lateinit var cardViewPlayer1: CardView
    private lateinit var cardNumberPlayer1: TextView
    private lateinit var cardSuitPlayer1: ImageView
    private lateinit var totalDiscardPilePlayer1: TextView
    private lateinit var totalRegularPilePlayer1: TextView
    private lateinit var textDiscardPilePlayer1: TextView
    private lateinit var roundStatus: TextView
    private lateinit var statusGame: TextView
    private lateinit var imageRiffleGame: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        configIdsXml()
        resetGame()
        actionResetButton()
        game()
    }

    private fun configIdsXml() {
        instructionsPlayer2 = findViewById(R.id.activity_game_instructions_player2)
        cardViewPlayer2 = findViewById(R.id.activity_game_round_cardView_player2)
        cardNumberPlayer2 = findViewById(R.id.activity_game_round_card_number_player2)
        cardSuitPlayer2 = findViewById(R.id.activity_game_round_card_suit_player2)
        totalDiscardPilePlayer2 = findViewById(R.id.activity_game_discard_pile_total_cards_player2)
        totalRegularPilePlayer2 = findViewById(R.id.activity_game_regular_pile_total_cards_player2)
        textDiscardPilePlayer2 = findViewById(R.id.activity_game_discard_pile_cards_player2)
        instructionsPlayer1 = findViewById(R.id.activity_game_instructions_player1)
        cardViewPlayer1 = findViewById(R.id.activity_game_round_cardView_player1)
        cardNumberPlayer1 = findViewById(R.id.activity_game_round_card_number_player1)
        cardSuitPlayer1 = findViewById(R.id.activity_game_round_card_suit_player1)
        totalDiscardPilePlayer1 = findViewById(R.id.activity_game_discard_pile_total_cards_player1)
        totalRegularPilePlayer1 = findViewById(R.id.activity_game_regular_pile_total_cards_player1)
        textDiscardPilePlayer1 = findViewById(R.id.activity_game_discard_pile_cards_player1)
        roundStatus = findViewById(R.id.activity_game_round_number)
        statusGame = findViewById(R.id.activity_game_status_game)
        imageRiffleGame = findViewById(R.id.activity_game_image_start_game)
    }

    private fun game() {
        if(!viewModel.checkEndGame()){
            instructionsPlayers()
        }else{
            instructionsEndGame()
        }
    }

    private fun instructionsEndGame() {
        statusGame.visibility = View.VISIBLE
        statusGame.text = viewModel.checkGamerWinner(false)
    }

    private fun instructionsPlayers() {
        instructionsOnPlayer1Turn()
    }

    private fun instructionsOnPlayer1Turn() {
        instructionsPlayer1.visibility = View.VISIBLE
        instructionsPlayer1.text = getString(R.string.instruction_player_turn)
        roundStatus.visibility=View.VISIBLE
        roundStatus.text=viewModel.getRoundStatus()
        instructionsPlayer1.setOnClickListener {
            player1RoundCard = viewModel.getRandomCardPlayer1()
            cardNumberPlayer1.text = checkCardName(player1RoundCard!!.card)
            cardViewPlayer1.visibility = View.VISIBLE
            cardSuitPlayer1.setImageResource(checkSuitImage(player1RoundCard!!.suit))
            cardSuitPlayer1.visibility = View.VISIBLE
            instructionsPlayer1.visibility = View.INVISIBLE
            instructionsPlayer2Turn()
        }
    }

    private fun instructionsPlayer2Turn() {
        instructionsPlayer2.visibility = View.VISIBLE
        instructionsPlayer2.text = getString(R.string.instruction_player_turn)
        instructionsPlayer2.setOnClickListener {
            player2RoundCard = viewModel.getRandomCardPlayer2()
            cardNumberPlayer2.text = checkCardName(player2RoundCard!!.card)
            cardViewPlayer2.visibility = View.VISIBLE
            cardSuitPlayer2.setImageResource(checkSuitImage(player2RoundCard!!.suit))
            cardSuitPlayer2.visibility = View.VISIBLE
            instructionsPlayer2.visibility = View.INVISIBLE
            viewModel.doRoad(player1RoundCard!!, player2RoundCard!!)
            val whoWinRound = viewModel.whoWin(player1RoundCard!!, player2RoundCard!!)
            waitTimeToShowWinner(whoWinRound)
        }
    }

    private fun waitTimeToShowWinner(whoWinRound: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            checkInstructionsForWinnerRound(whoWinRound)
            waitingTimeToNextRound()
        }, 1000)
    }

    private fun waitingTimeToNextRound() {
        Handler(Looper.getMainLooper()).postDelayed({
            cardViewPlayer1.visibility = View.INVISIBLE
            cardViewPlayer2.visibility = View.INVISIBLE
            cardViewPlayer1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            cardViewPlayer2.setBackgroundColor(Color.parseColor("#FFFFFF"))
            statusGame.visibility = View.INVISIBLE
            settingsPlayersCard()
            game()
        }, 2000)
    }

    private fun checkInstructionsForWinnerRound(whoWinRound: Int) {
        if (whoWinRound == 1) {
            cardViewPlayer1.setBackgroundColor(Color.parseColor("#A102F670"))
            cardViewPlayer2.setBackgroundColor(Color.parseColor("#7CF60000"))
            statusGame.visibility = View.VISIBLE
            statusGame.text = "${getString(R.string.player1)}\n${getString(R.string.win_round)}"


                } else if (whoWinRound == 2) {
                    cardViewPlayer2.setBackgroundColor(Color.parseColor("#A102F670"))
                    cardViewPlayer1.setBackgroundColor(Color.parseColor("#7CF60000"))
                    statusGame.visibility = View.VISIBLE
                    statusGame.text = "${getString(R.string.player2)}\n${getString(R.string.win_round)}"
                        }
                        }

    private fun settingsShowPlayersCard() {
        var discardPileCardsPlayer1 = ""
        viewModel.getCardsPlayer1().filter { !it.regularPile }.forEach {
            discardPileCardsPlayer1 =
                "${checkCardName(it.card)}(${checkSuitName(it.suit)}) $discardPileCardsPlayer1"
        }
        var discardPileCardsPlayer2 = ""

        viewModel.getCardsPlayer2().filter { !it.regularPile }.forEach {
            discardPileCardsPlayer2 =
                "${checkCardName(it.card)}(${checkSuitName(it.suit)}) $discardPileCardsPlayer2"
        }

        textDiscardPilePlayer2.visibility = View.VISIBLE
        textDiscardPilePlayer1.visibility = View.VISIBLE
        textDiscardPilePlayer1.text = discardPileCardsPlayer1
        textDiscardPilePlayer2.text = discardPileCardsPlayer2
    }

    private fun settingsTotalPlayersCards() {
        totalRegularPilePlayer1.text =
            "${viewModel.getCardsPlayer1().filter { it.regularPile }.size} ${getString(R.string.cards)}"
        totalRegularPilePlayer2.text =
            "${viewModel.getCardsPlayer2().filter { it.regularPile }.size} ${getString(R.string.cards)}"
                totalDiscardPilePlayer1.text =
                    "${viewModel.getCardsPlayer1().filter { !it.regularPile }.size} ${getString(R.string.cards)}"
                        totalDiscardPilePlayer2.text =
                            "${viewModel.getCardsPlayer2().filter { !it.regularPile }.size} ${getString(R.string.cards)}"
                                }

    private fun actionResetButton() {
        val resetButton = findViewById<Button>(R.id.activity_game_button_reset_game)
        resetButton.setOnClickListener {
            showAnimationStartGame()
            waitTimeToResetGame()
        }
    }

    private fun resetGame() {
        viewModel.inicialGame()
        associateSuitsGame()
        game()
        statusGame.visibility=View.INVISIBLE
        settingsPlayersCard()
    }

    private fun settingsPlayersCard() {
        settingsTotalPlayersCards()
        settingsShowPlayersCard()
    }

    private fun associateSuitsGame() {
        val suit = viewModel.getSuit()

        val suitPower1 = findViewById<ImageView>(R.id.activity_game_image_suit_power_1)
        val suitPower2 = findViewById<ImageView>(R.id.activity_game_image_suit_power_2)
        val suitPower3 = findViewById<ImageView>(R.id.activity_game_image_suit_power_3)
        val suitPower4 = findViewById<ImageView>(R.id.activity_game_image_suit_power_4)

        for (i in suit.indices) {
            when (suit[i].power) {
                1 -> suitPower1.setImageResource(checkSuitImage(suit[i].suit))
                2 -> suitPower2.setImageResource(checkSuitImage(suit[i].suit))
                3 -> suitPower3.setImageResource(checkSuitImage(suit[i].suit))
                4 -> suitPower4.setImageResource(checkSuitImage(suit[i].suit))
            }
        }
    }

    private fun checkSuitImage(suit: Int): Int {
        return when (suit) {
            SUIT_CLUBS -> R.drawable.suit_clubs
            CARD_DIAMONDS -> R.drawable.suit_diamond
            CARD_HEARTS -> R.drawable.suit_hearts
            CARD_SPADES -> R.drawable.suit_spades
            else -> R.drawable.ic_launcher_background
        }
    }

    private fun checkCardName(card: Int): String {
        return when (card) {
            in CARD_2..CARD_10 -> {
                (card+2).toString()
            }
            CARD_J -> "J"
            CARD_Q -> "Q"
            CARD_K -> "K"
            CARD_A -> "A"
            else -> "?"
        }
    }
    private fun checkSuitName(suit: Int): String {
        return when (suit) {
            SUIT_CLUBS -> "${getString(R.string.suit_clubs)}"
                CARD_DIAMONDS -> "${getString(R.string.suit_diamonds)}"
                    CARD_HEARTS -> "${getString(R.string.suit_hearts)}"
                        CARD_SPADES -> "${getString(R.string.suit_spades)}"
                            else -> "?"
                            }
                            }

    private fun showAnimationStartGame() {
        statusGame.visibility=View.INVISIBLE
        imageRiffleGame.visibility=View.VISIBLE
        Glide.with(this).load(R.drawable.riffle).into(imageRiffleGame)
    }

    private fun waitTimeToResetGame() {
        Handler(Looper.getMainLooper()).postDelayed({
            imageRiffleGame.visibility=View.GONE
            resetGame()
        }, 4000)
    }


}