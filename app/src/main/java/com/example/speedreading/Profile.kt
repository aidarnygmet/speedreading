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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    private lateinit var auth: FirebaseAuth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
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
        auth = Firebase.auth
        signin.setOnClickListener {
//            GlobalScope.launch() {
//            val usersList = usersApi.getAllUsers().await()
//            val user = users(username.text.toString(), password.text.toString(),  -1)
//            var success = 0
//            usersList.forEach{
//                if(user.username == it.username && user.password == it.password){
//                    success = 1
//                    bundle = Bundle()
//                    bundle.putString("key", it.user_id.toString())
//                    parentFragmentManager.setFragmentResult("userId", bundle)
//                }
//            }
//                if(success != 0){
//                    Log.d("test", "Signed in")
//                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing in successful", Toast.LENGTH_SHORT).show()})
//                    val fragment_user = fragment_user()
//                    fragment_user.arguments= bundle
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragmentContainerView1, fragment_user).commit()
//                } else {
//                    Log.d("test", "Sign in failed")
//                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing in failed", Toast.LENGTH_SHORT).show()})
//                }
//
//            }
            val email = username.text.toString()
            val password = password.text.toString()
            if(!email.matches(emailPattern.toRegex())){
                Log.d("test", "email incorrect")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Enter a proper email", Toast.LENGTH_SHORT).show()})
            } else if(password.isEmpty() || password.length<8){
                Log.d("test", "password incorrect")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Enter a proper password", Toast.LENGTH_SHORT).show()})
            } else {
                activity?.let { it ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(it) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("test", "SignWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("test", "SignWithEmail:failure", task.exception)
                                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()})

                            }
                        }
                }
            }
        }
        signup.setOnClickListener {
//            GlobalScope.launch(){
//                val user = users(username.text.toString(), password.text.toString(),  -1)
//                val response = usersApi.save(user).awaitResponse()
//                if (response.isSuccessful) {
//                    Log.d("test", "Sign up succesfull")
//                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing up successful", Toast.LENGTH_SHORT).show()})
//                } else {
//                    Log.d("test", "Sign up failed")
//                    activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Signing up failed", Toast.LENGTH_SHORT).show()})
//                }
//            }
            val email = username.text.toString()
            val password = password.text.toString()
            if(!email.matches(emailPattern.toRegex())){
                Log.d("test", "email incorrect")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Enter a proper email", Toast.LENGTH_SHORT).show()})
            } else if(password.isEmpty() || password.length<8){
                Log.d("test", "password incorrect")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Enter a proper password", Toast.LENGTH_SHORT).show()})
            } else {
                activity?.let { it1 ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("test", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("test", "createUserWithEmail:failure", task.exception)
                                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()})

                            }
                        }
                }
            }
        }

        return view
    }

    private fun updateUI(user: FirebaseUser?) {
        bundle = Bundle()
        if (user != null) {
            bundle.putString("key", user.uid)
            bundle.putString("email", username.text.toString())
            parentFragmentManager.setFragmentResult("userId", bundle)
            val fragment_user = fragment_user()
            fragment_user.arguments= bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, fragment_user).commit()
            Log.d("test", "wtf")
        } else {
            activity?.runOnUiThread { Toast.makeText(activity, "User already exists", Toast.LENGTH_SHORT) }
        }

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