package com.example.speedreading

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import kotlin.properties.Delegates

class Reading : AppCompatActivity() {
    private var listToRead = listOf<String>()
    private var speed = 0
    private var size: Float = 0.0F
    private var lastWord = 0
    private var isRunning = true

    private lateinit var file: File
    private lateinit var textUri: String
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
            textUri = extras.getString("textUri").toString()
            speed = extras.getInt("speed")
            size = extras.getFloat("size")
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size*50)
            wpm.text = speed.toString()
        }

        printwords2()
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
        var g = application.contentResolver.openInputStream(textUri.toUri())
        val reader = BufferedReader(g?.reader() )
        var content: String
        reader.use { reader ->
            content = reader.readText()
        }
        listToRead = content.split(" ")
        GlobalScope.launch {
            val x = lastWord
            for (i in x until listToRead.size){
                if (isRunning){
                    runOnUiThread {
                        text.text = listToRead[i]
//                        Log.d("test", "printWords: ${text.text}")
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
    private fun printwords2(){
        var g = application.contentResolver.openInputStream(textUri.toUri())
        val reader = BufferedReader(g?.reader() )
        var content: String
        reader.use { reader ->
            content = reader.readText()
        }
        listToRead = content.split(" ")
        GlobalScope.launch{
            val x = lastWord
            for (i in x until listToRead.size){
                if (isRunning){
                    runOnUiThread {

                        var current:String = listToRead[i]
                        var start: Int = 0
                        var end: Int = start + current.length
                        var toprint:String=""
                        if(i>0){
                            var first:String =  listToRead[i-1]
                            start = first.length+1
                            end = first.length +current.length+1
                            toprint = toprint+first+" "
                        }
                        toprint = toprint+current
                        if(i <listToRead.size-1){
                            var nextWord:String =listToRead[i+1]
                            toprint = toprint+" "+nextWord
                        }
                        text.setText(toprint, TextView.BufferType.SPANNABLE)
                        var s:Spannable = text.text as Spannable
                        s.setSpan(ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//                        Log.d("test", "printWords: ${text.text}")
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