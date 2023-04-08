package com.example.speedreading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
//import android.synthetic.main.activity_main.*
import java.util.*

class WordsPair : AppCompatActivity() {

    private val sameWordList = arrayOf(
        "apple\napple",
        "tiger\ntiger",
        "chair\nchair",
        "smile\nsmile",
        "creek\ncreek",
        "grape\ngrape",
        "whale\nwhale",
        "laugh\nlaugh",
        "stink\nstink",
        "spoon\nspoon",
        "heart\nheart",
        "blink\nblink",
        "elbow\nelbow",
        "lemon\nlemon",
        "cheese\ncheese",
        "faith\nfaith",
        "tower\ntower",
        "tramp\ntramp",
        "wagon\nwagon",
        "sugar\nsugar",
        "forge\nforge",
        "same\nsame",
        "moon\nmoon",
        "socks\nsocks",
        "bridge\nbridge",
        "pencil\npencil",
        "window\nwindow",
        "hex\nhex",
        "ocean\nocean",
        "brain\nbrain",
        "bird\nbird",
        "tree\ntree",
        "flower\nflower",
        "sweep\nsweep",
        "star\nstar",
        "river\nriver",
        "book\nbook",
        "beach\nbeach",
        "sun\nsun",
        "cloud\ncloud",
        "fish\nfish",
        "shoe\nshoe",
        "door\ndoor",
        "light\nlight",
        "bus\nbus",
        "cake\ncake",
        "phone\nphone",
        "water\nwater",
        "watch\nwatch",
        "moon\nmoon"
    )

    private val diffWordsList = arrayOf(
        "Flake\nFake",
        "Brain\nGrain",
        "Beach\nPeach",
        "Trial\nTrail",
        "Clasp\nGrasp",
        "Place\nPlate",
        "Light\nSight",
        "Trick\nTrack",
        "Bread\nBroad",
        "Sweep\nSleep",
        "Clean\nClear",
        "Crash\nCrush",
        "Crisp\nGrisp",
        "Flour\nFloor",
        "Forge\nForgo",
        "Ledge\nLodge",
        "Stone\nStove",
        "Swirl\nTwirl",
        "Tread\nTrade",
        "mine\nnine",
        "mat\noat",
        "man\npan",
        "main\nrain",
        "met\nset",
        "men\nten",
        "beam\nbeau",
        "same\nsave",
        "mind\nwind",
        "hem\nhex",
        "them\nthey",
        "prime\nprize"
    )
    private val random = Random()

    private var score = 0
    private var localScore = 0
    private var clickedButtons = mutableListOf<String>()

    private lateinit var buttons: Array<Button>
    private lateinit var btnStart: Button
    private lateinit var tvScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordspair)


        buttons = Array(15) { findViewById(resources.getIdentifier("btn${it+1}", "id", packageName)) }
        btnStart = findViewById(R.id.btnStart)
        tvScore = findViewById(R.id.tvScore)

        btnStart.setOnClickListener {
            startGame()
        }

        setButtonsClickable(false)
    }

    private fun startGame() {
        score = 0
        tvScore.text = "Score: $score"

        setButtonsClickable(true)

        val randomWords = selectRandomEntries(sameWordList, diffWordsList)

        for (i in 0..14) {
            setButtonWords(buttons[i], randomWords[i])
        }

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                btnStart.text = "Time left: $secondsLeft"
            }

            override fun onFinish() {
                btnStart.text = "Start"
                setButtonsClickable(false)
                score = 0
                tvScore.text = "Score: $score"
            }
        }.start()
    }

    private fun selectRandomEntries(arr: Array<String>, diffArr: Array<String>): Array<String> {
        val selectedEntries = mutableListOf<String>()

        // Select 4 random entries from arr
        val arrIndices = arr.indices.shuffled().take(10)
        arrIndices.forEach { index ->
            selectedEntries.add(arr[index])
        }

        // Select 2 random entries from diffArr
        val diffIndices = diffArr.indices.shuffled().take(5)
        diffIndices.forEach { index ->
            selectedEntries.add(diffArr[index])
        }
        selectedEntries.shuffle()

        // Return the selected entries as an array
        return selectedEntries.toTypedArray()
    }

    private fun resetGame() {
        clickedButtons.clear()
        localScore = 0
        val randomWords = selectRandomEntries(sameWordList, diffWordsList)
        for (i in buttons.indices) {
            setButtonWords(buttons[i], randomWords[i])
        }
    }

    private fun setButtonWords(button: Button, words: String) {
        button.text = words
        button.setOnClickListener {
            if (words.split("\n")[0] != words.split("\n")[1] && words !in clickedButtons) {
                clickedButtons.add(words)
                button.setBackgroundColor(Color.GREEN)
                score++
                localScore++
                tvScore.text = "Score: $score"

                Handler(Looper.getMainLooper()).postDelayed({
                    button.setBackgroundColor(getColor(R.color.butColor))
                }, 100)

                if (localScore == 5) {
                    resetGame()
                }
            }
        }
    }

    private fun setButtonsClickable(clickable: Boolean) {
        for (i in 0..14) {
            buttons[i].isClickable = clickable
        }

    }
}