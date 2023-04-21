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
    private  lateinit var user_id: String
    private lateinit var countDown: TextView
    private lateinit var list: Array<TextView>
    private val exercise_id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "los launched")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_los)
        pressed = false
        actionButton = findViewById(R.id.button2)
        countDown = findViewById(R.id.textView26)
        list = arrayOf(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6),
            findViewById(R.id.textView7),
            findViewById(R.id.textView8),
            findViewById(R.id.textView9),
            findViewById(R.id.textView10),
            findViewById(R.id.textView11),
            findViewById(R.id.textView12),
            findViewById(R.id.textView13),
            findViewById(R.id.textView14),
            findViewById(R.id.textView15),
            findViewById(R.id.textView16),
            findViewById(R.id.textView17),
            findViewById(R.id.textView18),
            findViewById(R.id.textView19),
            findViewById(R.id.textView20),
            findViewById(R.id.textView21),
            findViewById(R.id.textView22),
            findViewById(R.id.textView23),
            findViewById(R.id.textView24),
            findViewById(R.id.textView25)
        )

        var changed = false
        var total = 0
        var count = 0
        var countDownTimer = 40
        val extras = intent.extras
        if(extras != null){
            user_id = extras.getString("userId").toString()
            Log.d("test", "los $user_id")
        }
        var performanceApi = Retrofit.Builder().baseUrl("http://192.168.190.88:8080").addConverterFactory(GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)
        actionButton.setOnClickListener {
            if(pressed){

                if(changed){
                    count+=1
                }
            } else {
                pressed = true
                actionButton.text = "Mistake"
                for(i in list){
                    i.text = " ${getRandomString(1)} "
                }
                list[12].text = "*"
                var randomSymbol = ""
                var prevRandomSymbol = ""
                countDown.text = countDownTimer.toString()
                GlobalScope.launch {
                    for (i in 1..40){

                        countDownTimer -= 1
                        var list2 = list[2].text.toString()
                        var list10 = list[10].text.toString()
                        var list14 = list[14].text.toString()
                        var list22 = list[22].text.toString()
                        while(randomSymbol == prevRandomSymbol){
                            randomSymbol = getRandomString(1)
                        }


                        var changedSymbol = randomSymbol
                        if((0..1).random() == 1){
                            changed=true
                            total+=1
                            while(changedSymbol == randomSymbol){
                                changedSymbol = getRandomString(1)
                            }
                        }


                            var randomNum = (0..3).random()
                            when (randomNum) {
                                0 -> {
                                    list2 = " $changedSymbol "
                                    list10 = " $randomSymbol "
                                    list14 = " $randomSymbol "
                                    list22 = " $randomSymbol "
                                }
                                1 -> {
                                    list2 = " $randomSymbol "
                                    list10 = " $changedSymbol "
                                    list14 = " $randomSymbol"
                                    list22 = " $randomSymbol "
                                }
                                2 -> {
                                    list2 = " $randomSymbol "
                                    list10 = " $randomSymbol "
                                    list14 = " $changedSymbol"
                                    list22 = " $randomSymbol "
                                }
                                3 -> {
                                    list2 = " $randomSymbol "
                                    list10 = " $randomSymbol "
                                    list14 = " $randomSymbol"
                                    list22 = " $changedSymbol "
                                }
                            }
                            runOnUiThread {
                                list[2].text = list2
                                list[10].text = list10
                                list[14].text = list14
                                list[22].text = list22
                            }


                        runOnUiThread{
                            countDown.text = countDownTimer.toString()
                        }
                        delay(1000)
                        changed = false
                        prevRandomSymbol = randomSymbol
                    }
                    runOnUiThread{
                        countDown.text = "exercise finished"
                    }
                    var date = Date()
                    val performance = performance(exercise_id, user_id, (count/total)*100, date.toString())
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