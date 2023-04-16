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
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.slider.Slider
import java.io.BufferedReader
import java.io.File


class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment_main = MainFragment()
        val fragment_exercises = exercises()
        val fragment_profile = Profile()
        setCurrentFragment(fragment_main)
        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom)
        myBottomNavigationView.setOnNavigationItemSelectedListener {item->
            when(item.itemId){
                R.id.reading -> setCurrentFragment(fragment_main)
                R.id.exercises -> setCurrentFragment(fragment_exercises)
                R.id.profile -> setCurrentFragment(fragment_profile)
            }
            true
        }
        /*myBottomNavigationView.OnItemSelectedListener{
            when(it.itemId){
                R.id.reading -> setCurrentFragment(fragment_main)
                R.id.exercises -> setCurrentFragment(fragment_exercises)
                R.id.profile -> setCurrentFragment(fragment_profile)
            }
            true
        }*/
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView1, fragment)
            commit()
        }
    }
}






