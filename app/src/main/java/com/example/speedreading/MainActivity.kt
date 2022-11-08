package com.example.speedreading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.Slider
import kotlinx.coroutines.*
import kotlinx.coroutines.delay



class MainActivity : AppCompatActivity() {
    private lateinit var speedText: TextView
    private lateinit var start: Button
    private lateinit var textSpeed: Slider
    private lateinit var textSize: Slider

    private var isRunning = false
    private var isContinuing = true
    private val textToRead = "Everyone is entitled to all the rights and freedoms set forth in this Declaration, without distinction of any kind, such as race, colour, sex, language, religion, political or other opinion, national or social origin, property, birth or other status. Furthermore, no distinction shall be made on the basis of the political, jurisdictional or international status of the country or territory to which a person belongs, whether it be independent, trust, non-self-governing or under any other limitation of sovereignty."
    private var listToRead = textToRead.split(" ")
    private var wordPerMin =300
    private lateinit var pause: Button
    private lateinit var layout: ConstraintLayout
    //var job: Job? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pause = Button(this)
        speedText = findViewById(R.id.speedText)
        start = findViewById(R.id.startButton)
        textSpeed = findViewById(R.id.textSpeed)
        textSize = findViewById(R.id.textSize)
        layout = findViewById(R.id.layout)
        wordPerMin = textSpeed.value.toInt()
        var lastWord = 0
        fun printWords(){
            GlobalScope.launch {
                val x= lastWord
                for (i in x until listToRead.size){
                    if (isContinuing and isRunning){
                        runOnUiThread { speedText.text = listToRead[i] }
                        lastWord = i
                        delay((60000/wordPerMin).toLong())
                        if(i == listToRead.size-1){
                            runOnUiThread { speedText.text = "" }
                            layout.removeView(pause)
                            isRunning = false
                            isContinuing = true
                            start.text = "Start"
                            lastWord = 0
                        }
                    } else {
                        break
                    }
                }
            }
        }
        start.setOnClickListener {
            if (isRunning){
                isRunning = false
                isContinuing = true
                start.text = "Start"
                speedText.text = ""
                layout.removeView(pause)
                lastWord = 0
            } else {
                Log.d("test", "message1")
                isRunning = true
                start.text = "Stop"
                pause.text="Pause"
                layout.addView(pause)
                printWords()
            }
        }

        pause.setOnClickListener {
            if(isContinuing){
                isContinuing = false
                pause.text = "continue"
            } else {
                isContinuing = true
                pause.text = "pause"
                printWords()
            }
        }
        textSize.addOnChangeListener{
                _, value, _ -> speedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value*50)
        }
        textSpeed.addOnChangeListener{
            _, value, _ ->wordPerMin = value.toInt()
        }
    }

}

