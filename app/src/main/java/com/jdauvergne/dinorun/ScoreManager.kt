package com.jdauvergne.dinorun

import android.annotation.SuppressLint
import android.widget.TextView
import com.jdauvergne.dinorun.display.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Context

class ScoreManager(
    private val context: Context,
    private val scoreTextView: TextView,
    private val highScoreTextView: TextView
) {
    private val PREFS_NAME = "game_prefs"
    private val SCORE_KEY = "saved_score"
    private val HIGH_SCORE_KEY = "high_score"
    private val DEFAULT_SCORE = 0
    private var job: Job? = null
    var score: Int = DEFAULT_SCORE
        private set
    var highScore: Int = DEFAULT_SCORE
        private set

    init {
        loadScores()
    }

    private fun saveScores() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(SCORE_KEY, score).apply()

        if (score > highScore) {
            highScore = score
            prefs.edit().putInt(HIGH_SCORE_KEY, highScore).apply()
        }
    }

    private fun loadScores() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        score = prefs.getInt(SCORE_KEY, DEFAULT_SCORE)
        highScore = prefs.getInt(HIGH_SCORE_KEY, DEFAULT_SCORE)
        updateScoreView()
        updateHighScoreView()
    }

    private fun incrementScore() {
        score += 5
        saveScores()
        updateScoreView()
    }


    @SuppressLint("SetTextI18n")
    private fun updateScoreView(){
        scoreTextView.text = "Score: ${score.toString().padStart(5, '0')}"
    }
    @SuppressLint("SetTextI18n")
    private fun updateHighScoreView(){
        highScoreTextView.text = "High Score: ${highScore.toString().padStart(5, '0')}"
    }

    fun resetScore() {
        score = DEFAULT_SCORE
        saveScores()
        updateScoreView()
    }

    fun start() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (job?.isActive == true) {
                incrementScore()
                if (score % 500 == 0) {
                    MainActivity.gameSpeed -= 50
                    println("Time to speed up ! : ${MainActivity.gameSpeed}")
                }
                delay(100)
            }
        }
    }

    fun stop(){
        job?.cancel()
    }
}