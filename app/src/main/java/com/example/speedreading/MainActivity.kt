package com.example.speedreading

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var speedText: TextView
    private lateinit var start: ImageButton
    private lateinit var textSpeed: Slider
    private lateinit var textSize: Slider
    private lateinit var textInput: EditText
    private lateinit var submit: Button
    private var textToRead = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submit = findViewById(R.id.submit)
        textInput = findViewById(R.id.textInput)
        speedText = findViewById(R.id.speedText)
        start = findViewById(R.id.startButton)
        textSpeed = findViewById(R.id.textSpeed)
        textSize = findViewById(R.id.textSize)
        submit.setOnClickListener {
            textToRead = textInput.text.toString()
            Log.d("test", textToRead)
        }
        start.setOnClickListener(View.OnClickListener { v: View? ->
            // Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining
            // the components you are targeting. Intent to start an activity called SecondActivity with the following code.
            val intent = Intent(this, reading::class.java)
            // start the activity connect to the specified class
            intent.putExtra("text", textToRead)
            intent.putExtra("speed", textSpeed.value.toInt())
            intent.putExtra("size", textSize.value)
            startActivity(intent)
        })
        textSize.addOnChangeListener{
                _, value, _ -> speedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value*50)
        }

    }
//        var lastWord = 0
//        fun printWords(){
//            GlobalScope.launch {
//                val x= lastWord
//                for (i in x until listToRead.size){
//                    if (isContinuing and isRunning){
//                        runOnUiThread { speedText.text = listToRead[i] }
//                        lastWord = i
//                        delay((60000/wordPerMin).toLong())
//                        if(i == listToRead.size-1){
//                            runOnUiThread { speedText.text = "" }
//                            layout.removeView(pause)
//                            isRunning = false
//                            isContinuing = true
//                            start.text = "Start"
//                            lastWord = 0
//                        }
//                    } else {
//                        break
//                    }
//                }
//            }
//        }
//        start.setOnClickListener {
//            if (isRunning){
//                isRunning = false
//                isContinuing = true
//                start.text = "Start"
//                speedText.text = ""
//                layout.removeView(pause)
//                lastWord = 0
//            } else {
//                Log.d("test", "message1")
//                isRunning = true
//                start.text = "Stop"
//                pause.text="Pause"
//                layout.addView(pause)
//                printWords()
//            }
//        }
//
//        pause.setOnClickListener {
//            if(isContinuing){
//                isContinuing = false
//                pause.text = "continue"
//            } else {
//                isContinuing = true
//                pause.text = "pause"
//                printWords()
//            }
//        }

//        textSpeed.addOnChangeListener{
//            _, value, _ ->wordPerMin = value.toInt()
//        }
//    }

}

