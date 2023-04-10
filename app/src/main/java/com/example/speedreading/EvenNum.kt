package com.example.speedreading

//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import com.example.evennumbers.databinding.ActivityMainBinding

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private lateinit var buttons: Array<Button>

private val random = Random()

class EvenNum : AppCompatActivity() {

    private var evenNumbers = mutableListOf<Int>()
    private var score = 0
    private var globalScore = 0
    private var clickedNumbers = mutableListOf<Int>()
    private var remainingTimeMillis = 2 * 60 * 1000L

    private lateinit var user_id:String
    private val exercise_id = 2
    private val performanceApi = Retrofit.Builder().baseUrl("http://192.168.1.64:8080").addConverterFactory(
        GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)


//    var date = Date()
//    val performance = performance(exercise_id, user_id, score, date.toString())
//    GlobalScope.launch{
//        val response = performanceApi.save(performance).awaitResponse()
//        if (response.isSuccessful) {
//            Log.d("test", "success")
//        } else {
//            Log.d("test", "fail")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_even_num)
        findViewById<Button>(R.id.start_button).setOnClickListener {
            resetGame()
            startTimer()
        }
        val extras = intent.extras
        if(extras != null){
        user_id = extras.getString("userId").toString()
        Log.d("test", "evennum $user_id")
    }
        buttons = Array(40) { findViewById(resources.getIdentifier("button${it + 1}", "id", packageName)) }
    }

    private fun generateEvenNumbers(): MutableList<Int> {
        val evenNumbers = mutableListOf<Int>()
        while (evenNumbers.size < 4) {
            val number = (1000..9999).random()
            if (number % 2 == 0 && number !in evenNumbers) {
                evenNumbers.add(number)
            }
        }
        val nonEvenNumbers = mutableListOf<Int>()
        while (nonEvenNumbers.size < 36) {
            val number = (1000..9999).random()
            if (number % 2 != 0 && number !in evenNumbers && number !in nonEvenNumbers) {
                nonEvenNumbers.add(number)
            }
        }
        evenNumbers.addAll(nonEvenNumbers)
        evenNumbers.shuffle()
        return evenNumbers
    }

    private fun updateButtons(buttons: Array<Button>, numbers: List<Int>) {
        for ((i, button) in buttons.withIndex()) {
            button.text = numbers[i].toString()
        }
    }

    private fun clearButtons(buttons: Array<Button>) {
        for ((i, button) in buttons.withIndex()) {
            button.text = ""
        }
    }

    private fun resetGame() {
        evenNumbers = generateEvenNumbers()
        buttons = Array(40) { findViewById(resources.getIdentifier("button${it + 1}", "id", packageName)) }
        updateButtons(buttons, evenNumbers)
        score = 0
        clickedNumbers.clear()
    }

    private fun startTimer() {
        findViewById<Button>(R.id.timer_button).apply { text = "02:00" }
        object : CountDownTimer(remainingTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeMillis = millisUntilFinished
                updateTimerText()
            }
            override fun onFinish() {
                remainingTimeMillis = 0
                updateTimerText()
                showScore()
                clearButtons(buttons)
            }
        }.start()
        buttons.forEach { button ->
            button.setOnClickListener {
                val number = button.text.toString().toInt()
                if (number % 2 == 0 && number !in clickedNumbers) {
                    clickedNumbers.add(number)
                    score++
                    globalScore++
                    button.setBackgroundColor(Color.GREEN)
                    Handler(Looper.getMainLooper()).postDelayed({
                        button.setBackgroundColor(getColor(R.color.butColor))
                    }, 500)
                    if (score == 4) {
                        showScore()
                        resetGame()
                    }
                }
            }
        }
    }

    private fun updateTimerText() {
        val minutes = (remainingTimeMillis / 1000) / 60
        val seconds = (remainingTimeMillis / 1000) % 60
        val timeText = String.format("%02d:%02d", minutes, seconds)
        findViewById<Button>(R.id.timer_button).text = timeText
    }

    private fun showScore() {
        val message = "Your score is ${globalScore}"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putLong("remainingTimeMillis", remainingTimeMillis)
        outState.putIntegerArrayList("evenNumbers", ArrayList(evenNumbers))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score", 0)
        remainingTimeMillis = savedInstanceState.getLong("remainingTimeMillis", 2 * 60 * 1000L)
        evenNumbers = savedInstanceState.getIntegerArrayList("evenNumbers")?.toMutableList() ?: generateEvenNumbers()
        updateTimerText()
        buttons = Array(40) { findViewById(resources.getIdentifier("button${it + 1}", "id", packageName)) }

        updateButtons(buttons, evenNumbers)
    }
}