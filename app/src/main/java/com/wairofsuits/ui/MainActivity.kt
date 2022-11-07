package com.wairofsuits.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wairofsuits.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStart = findViewById<Button>(R.id.activity_main_button_start_game)
        val imageStartGame = findViewById<ImageView>(R.id.activity_main_image_start_game)
        buttonStart.setOnClickListener {
            showAnimationStartGame(buttonStart, imageStartGame)
            waitTimeToStartGame(buttonStart, imageStartGame)
        }
    }

    private fun showAnimationStartGame(buttonStart: Button, imageStartGame: ImageView) {
        buttonStart.visibility= View.INVISIBLE
        imageStartGame.visibility=View.VISIBLE
        Glide.with(this).load(R.drawable.riffle).into(imageStartGame)
    }

    private fun waitTimeToStartGame(buttonStart: Button, imageStartGame: ImageView) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            originalState(buttonStart, imageStartGame)
        }, 4000)
    }

    private fun originalState(buttonStart: Button, imageStartGame: ImageView) {
        buttonStart.visibility= View.VISIBLE
        imageStartGame.visibility=View.INVISIBLE
    }
}