package com.example.speedreading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    lateinit var speedText: TextView
    lateinit var start: Button
    var isRunning = false
    private val textToRead = "Everyone is entitled to all the rights and freedoms set forth in this Declaration, without distinction of any kind, such as race, colour, sex, language, religion, political or other opinion, national or social origin, property, birth or other status. Furthermore, no distinction shall be made on the basis of the political, jurisdictional or international status of the country or territory to which a person belongs, whether it be independent, trust, non-self-governing or under any other limitation of sovereignty."
    private var listToRead = textToRead.split(" ")
    private var wordPerMin = 300
    var job: Job? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speedText = findViewById(R.id.speedText)
        start = findViewById(R.id.startButton)


        start.setOnClickListener {
            if (isRunning){
                isRunning = false
                start.text = "Start"
                job?.cancel()
            } else {
                isRunning = true
                start.text = "Stop"
                job = GlobalScope.launch {
                    for (word in listToRead){
                       runOnUiThread { speedText.text = word }
                        delay((60000/wordPerMin).toLong())
                    }
                }

            }
        }
    }
    }
