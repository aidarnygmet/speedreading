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

    private lateinit var mainButton: Button
    private lateinit var exercisesButton: Button
    private lateinit var profileButton: Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainButton = findViewById(R.id.button6)
        exercisesButton = findViewById(R.id.button5)
        profileButton = findViewById(R.id.button4)
        mainButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView1, MainFragment.newInstance("1", "2")).commit()
        }
        profileButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView1, Profile.newInstance("1", "2")).commit()
        }
        exercisesButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView1, exercises.newInstance("1", "2")).commit()
        }

    }
}






