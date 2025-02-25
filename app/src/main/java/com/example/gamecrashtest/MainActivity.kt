package com.example.gamecrashtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.gamecrashtest.Tools.Companion.initScreenWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mainView:ConstraintLayout
    private lateinit var groundView:View
    private lateinit var scoreTextView:TextView

    private lateinit var dino:Dinosaur
    private var score = 0

    private var isGameLaunched = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main
        setContentView(layout)

        hideSystemUI()  //TEMP and bad
        initScreenWidth(this)

        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)

        dino = Dinosaur(findViewById(R.id.dinoImageView))

        val DEFAULT_SCORE = 0
        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = "$DEFAULT_SCORE"

        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchScreenResponse()
            }
            true
        }
    }

    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            isGameLaunched = true
            lifecycleScope.launch {
                dino.dinoStartingAnimation()
                dino.startSpriteCycle()
            }
            lifecycleScope.launch {
                delay(3000)
                cactusSpawner()
            }
        } else {
            runOnUiThread {
                dino.touchScreenResponse()
                addScore(1)
            }
        }
    }


    private fun cactusSpawner() {
        lifecycleScope.launch {
            val cactusGroupFactory = CactusGroupFactory(mainView)
            while (isActive) {
                //entries.random()
                val randomCactusGroup = CactusGroupsEnum.entries.random()
                println(randomCactusGroup)
                val cactusGroup = cactusGroupFactory.buildCactusGroup(randomCactusGroup)

                cactusGroup.spawn()
                cactusGroup.startMoving(lifecycleScope)

                delay(3000) //todo faire scale avec le score
            }
        }
    }

    private fun addScore(scoreToAdd:Int){
        val scoreRender = scoreTextView
        score += scoreToAdd
        scoreRender.text = "$score"
    }

    //TEMP and bad
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }


}