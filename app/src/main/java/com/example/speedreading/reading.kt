package com.example.speedreading

import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class Reading : AppCompatActivity() {
    private var listToRead = listOf<String>()
    private var speed = 0
    private var size: Float = 0.0F
    private var lastWord = 0
    private var isRunning = true
    private lateinit var text: TextView
    private lateinit var pause: ImageButton
    private lateinit var stop: ImageButton
    private lateinit var inc: ImageButton
    private lateinit var dec: ImageButton
    private lateinit var wpm: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        text = findViewById(R.id.speedText)
        pause = findViewById(R.id.pausereading)
        stop = findViewById(R.id.stopreading)
        inc = findViewById(R.id.increase)
        dec = findViewById(R.id.decrease)
        wpm = findViewById(R.id.textView3)
        val extras = intent.extras
        if (extras != null) {
            listToRead = extras.getString("text").toString().split(" ")
            speed = extras.getInt("speed")
            size = extras.getFloat("size")
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size*50)
            wpm.text = speed.toString()
        }

        printWords()
        pause.setOnClickListener {
            if(isRunning){
                isRunning = false
                pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            } else {
                isRunning = true
                pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                printWords()
            }
        }
        stop.setOnClickListener {
            finish()
        }
        dec.setOnClickListener {
            speed-=10
            wpm.text = speed.toString()
        }
        inc.setOnClickListener {
            speed+=10
            wpm.text = speed.toString()
        }
    }
    private fun printWords(){
            GlobalScope.launch {
                val x = lastWord
                for (i in x until listToRead.size){
                    if (isRunning){
                        runOnUiThread {
                            text.text = listToRead[i]
                        }
                        lastWord = i
                        delay((60000/speed).toLong())
                        if(i == listToRead.size-1){
                            finish()
                        }
                    } else {
                        break
                    }
                }
            }
        }
}