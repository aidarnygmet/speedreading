package com.example.speedreading

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates


class los : AppCompatActivity() {
    private lateinit var actionButton: Button
    private var pressed by Delegates.notNull<Boolean>()
    private var user_id by Delegates.notNull<Int>()
    private lateinit var firstRow: TextView
    private lateinit var secondRow: TextView
    private lateinit var thirdRow: TextView
    private lateinit var fourthRow: TextView
    private lateinit var fifthRow: TextView
    private lateinit var countDown: TextView
    private val exercise_id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "los launched")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_los)
        pressed = false
        actionButton = findViewById(R.id.button2)
        firstRow = findViewById(R.id.textView7)
        secondRow = findViewById(R.id.textView8)
        thirdRow = findViewById(R.id.textView9)
        fourthRow = findViewById(R.id.textView10)
        fifthRow = findViewById(R.id.textView11)
        countDown = findViewById(R.id.textView12)
        var changed = false
        var total = 0
        var count = 0
        var countDownTimer = 40
        val extras = intent.extras
        if(extras != null){

            user_id = extras.getString("userId")?.toInt()!!
            Log.d("test", "los "+user_id.toString())
        }
        var performanceApi = Retrofit.Builder().baseUrl("http://192.168.1.70:8080").addConverterFactory(GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)
        actionButton.setOnClickListener {
            if(pressed){

                if(changed){
                    count+=1
                }
            } else {
                pressed = true
                actionButton.text = "Mistake"
                firstRow.text = getRandomString(7)
                secondRow.text = getRandomString(7)
                thirdRow.text = getRandomString(7)
                fourthRow.text = getRandomString(7)
                fifthRow.text = getRandomString(7)
                countDown.text = countDownTimer.toString()
                GlobalScope.launch {
                    for (i in 1..40){
                        countDownTimer = countDownTimer -1
                        var firsttext = firstRow.text.toString()
                        var thirdtext = thirdRow.text.toString()
                        var fifthtext = fifthRow.text.toString()
                        var randomSymbol = getRandomString(1)

                        if((0..1).random() == 1){
                            changed=true
                            total+=1
                            var changedSymbol = getRandomString(1)
                            while(changedSymbol == randomSymbol){
                                changedSymbol = getRandomString(1)
                            }
                            var randomNum = (0..3).random()
                            if(randomNum == 0){
                                firsttext = firstRow.text.toString().substring(0..5)+changedSymbol+firstRow.text.toString().substring(7)
                                thirdtext = randomSymbol + thirdRow.text.toString().substring(1, 12) + randomSymbol
                                fifthtext = fifthRow.text.toString().substring(0..5)+randomSymbol+fifthRow.text.toString().substring(7)
                            } else if (randomNum == 1){
                                firsttext = firstRow.text.toString().substring(0..5)+randomSymbol+firstRow.text.toString().substring(7)
                                thirdtext = changedSymbol + thirdRow.text.toString().substring(1, 12) + randomSymbol
                                fifthtext = fifthRow.text.toString().substring(0..5)+randomSymbol+fifthRow.text.toString().substring(7)
                            } else if(randomNum == 2){
                                firsttext = firstRow.text.toString().substring(0..5)+randomSymbol+firstRow.text.toString().substring(7)
                                thirdtext = randomSymbol + thirdRow.text.toString().substring(1, 12) + changedSymbol
                                fifthtext = fifthRow.text.toString().substring(0..5)+randomSymbol+fifthRow.text.toString().substring(7)
                            } else if (randomNum == 3){
                                firsttext = firstRow.text.toString().substring(0..5)+randomSymbol+firstRow.text.toString().substring(7)
                                thirdtext = randomSymbol + thirdRow.text.toString().substring(1, 12) + randomSymbol
                                fifthtext = fifthRow.text.toString().substring(0..5)+changedSymbol+fifthRow.text.toString().substring(7)
                            }
                            runOnUiThread {
                                firstRow.text = firsttext
                                thirdRow.text = thirdtext
                                fifthRow.text = fifthtext
                            }

                        }
                        runOnUiThread{
                            countDown.text = countDownTimer.toString()
                        }
                        delay(1000)
                        changed = false
                    }
                    runOnUiThread{
                        countDown.text = "exercise finished"
                    }
                    val performance = performance(exercise_id, user_id, (count/total)*100, Calendar.getInstance().time.toString())
                    val response = performanceApi.save(performance).awaitResponse()
                    if (response.isSuccessful) {
                        Log.d("test", "success")
                    } else {
                        Log.d("test", "fail")
                    }
                }
            }
        }
    //        retrofitButton.setOnClickListener {
//            val performance = performance(exercise_id, user_id, 9,Calendar.getInstance().time.toString())
//            GlobalScope.launch(Dispatchers.IO) {
//
//                    val response = performanceApi.save(performance).awaitResponse()
//                    if (response.isSuccessful) {
//                        Log.d("test", "success")
//                    } else {
//                        Log.d("test", "fail")
//                    }
//
//            }
//        }
    }
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString(" ")
    }

}