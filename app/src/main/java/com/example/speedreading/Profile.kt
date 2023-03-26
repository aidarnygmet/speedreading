package com.example.speedreading

import android.os.Bundle
import android.util.Log
import java.util.Calendar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var signin: Button
    private lateinit var signup: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        signin = view.findViewById(R.id.signin)
        signup = view.findViewById(R.id.signup)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        var usersApi = Retrofit.Builder().baseUrl("http://192.168.1.64:8080").addConverterFactory(
            GsonConverterFactory.create()).build().create(com.example.speedreading.usersApi::class.java)
        signin.setOnClickListener {
            GlobalScope.launch() {
            val usersList = usersApi.getAllUsers().await()
            val user = users(username.text.toString(), password.text.toString(),  -1)
            var success = 0
            usersList.forEach{
                if(user.username == it.username && user.password == it.password){
                    success = 1
                    bundle = Bundle()
                    bundle.putString("key", it.user_id.toString())
                    parentFragmentManager.setFragmentResult("userId", bundle)
                }
            }
                if(success != 0){
                    Log.d("test", "Signed in")
                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing in successful", Toast.LENGTH_SHORT).show()})
                    val fragment_user = fragment_user()
                    fragment_user.arguments= bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView1, fragment_user).commit()
                } else {
                    Log.d("test", "Sign in failed")
                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing in failed", Toast.LENGTH_SHORT).show()})
                }

            }
        }
        signup.setOnClickListener {
            GlobalScope.launch(){
                val user = users(username.text.toString(), password.text.toString(),  -1)
                val response = usersApi.save(user).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("test", "Sign up succesfull")
                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing up successful", Toast.LENGTH_SHORT).show()})
                } else {
                    Log.d("test", "Sign up failed")
                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing up failed", Toast.LENGTH_SHORT).show()})
                }
            }
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
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}