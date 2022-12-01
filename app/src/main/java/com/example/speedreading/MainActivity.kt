package com.example.speedreading

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toFile
import com.google.android.material.slider.Slider
import java.io.BufferedReader
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var speedText: TextView
    private lateinit var start: ImageButton
    private lateinit var textSpeed: Slider
    private lateinit var textSize: Slider
    private lateinit var textInput: EditText
    private lateinit var submit: Button
    private lateinit var select: Button
    private var textToRead = ""

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri

        if (uri != null) {

            var g = application.contentResolver.openInputStream(uri)
            val reader = BufferedReader(g?.reader() )
            var content: String
            reader.use { reader ->
                textToRead = reader.readText()
            }

            Toast.makeText(this@MainActivity,"File is opened",Toast.LENGTH_SHORT).show()
        };
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submit = findViewById(R.id.submit)
        select = findViewById(R.id.selectButton)
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

        select.setOnClickListener {
            getContent.launch("text/*")

        }
    }


}






