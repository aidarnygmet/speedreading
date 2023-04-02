package com.example.speedreading

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Schulte : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private lateinit var startButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var user_id : String
    private val random = Random()
    private var actualScore = 0

    val numbers = MutableList(25) { index -> index + 1 }
    private val exercise_id = 2
    private val performanceApi = Retrofit.Builder().baseUrl("http://192.168.1.64:8080").addConverterFactory(
        GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)
    private lateinit var timer: CountDownTimer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schulte)
        val extras = intent.extras
        if(extras != null){
            user_id = extras.getString("userId").toString()
            Log.d("test", "schulte $user_id")
        }
        buttons = Array(25) { findViewById(resources.getIdentifier("button${it+1}", "id", packageName)) }

        startButton = findViewById(R.id.start_button)
        timerTextView = findViewById(R.id.timer_text_view)


        startButton.setOnClickListener {
            shuffleButtons()
            startTimer()
            startButton.text = "Restart"
        }
    }

    private fun shuffleButtons() {
        numbers.shuffle()
        buttons.forEachIndexed { index, button ->
            button.text = numbers[index].toString()
        }
    }

    private fun startTimer() {
        var prevValue = 0
        var count = 0
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                count++
                actualScore = count
                timerTextView.text = count.toString()
            }
            override fun onFinish() {
            }
        }
        timer.start()
        var score = 0
        buttons.forEach { button ->
            button.setOnClickListener {
                val value = button.text.toString().toInt()
                if (value > prevValue && value-prevValue == 1) {
                    button.setBackgroundColor(Color.GREEN)

                    // Delay for 500 milliseconds (half a second)
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Change the button color back to its original color
                        button.setBackgroundColor(getColor(R.color.butColor))
                    }, 500)
                    prevValue = value
                    score++
                    println(score)
                    if (score == 25) {
                        timer.cancel()
                        var date = Date()
                        val performance = performance(exercise_id, user_id, actualScore, date.toString())
                        GlobalScope.launch{
                            val response = performanceApi.save(performance).awaitResponse()
                            if (response.isSuccessful) {
                                Log.d("test", "success")
                            } else {
                                Log.d("test", "fail")
                            }
                        }

                    }
                }
            }
        }
    }
}