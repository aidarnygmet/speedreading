package com.example.speedreading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.slider.Slider
import java.io.BufferedReader
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class MainFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var speedText: TextView
    private lateinit var start: ImageButton
    private lateinit var textSpeed: Slider
    private lateinit var textSize: Slider
    private lateinit var textInput: EditText
    private lateinit var select: Button
    private lateinit var textToRead: String

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        if (uri != null) {
//            var file = File(uri.path)
//            var g = file.inputStream()
//            val reader = BufferedReader(g?.reader() )
//            reader.use { reader ->
//                textToRead = reader.readText()
//            }

            textToRead = uri.toString()
            Toast.makeText(activity,textToRead, Toast.LENGTH_SHORT).show()
        };

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //activity?.setContentView(R.layout.activity_main)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main, container, false)
        select = view.findViewById(R.id.selectButton)!!
        speedText = view.findViewById(R.id.speedText)!!
        start = view.findViewById(R.id.startButton)!!
        textSpeed = view.findViewById(R.id.textSpeed)!!
        textSize = view.findViewById(R.id.textSize)!!




        start.setOnClickListener(View.OnClickListener { v: View? ->
            // Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining
            // the components you are targeting. Intent to start an activity called SecondActivity with the following code.
            val intent = Intent(activity, reading::class.java)
            // start the activity connect to the specified class
            intent.putExtra("textUri", textToRead)
            intent.putExtra("speed", textSpeed.value.toInt())
            intent.putExtra("size", textSize.value)
            startActivity(intent)
        })


        textSize.addOnChangeListener{
                _, value, _ -> speedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value*50)
        }

        select.setOnClickListener {
            Log.d("test", "sampletext")
            getContent.launch("text/*")
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}