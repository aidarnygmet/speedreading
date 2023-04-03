package com.example.speedreading

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Signup.newInstance] factory method to
 * create an instance of this fragment.
 */
class Signup : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var signup: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirm: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var bundle: Bundle
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
        var view = inflater.inflate(R.layout.fragment_signup, container, false)
        signup = view.findViewById(R.id.signup)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        confirm = view.findViewById(R.id.confirm)
        auth = Firebase.auth
        // Inflate the layout for this fragment
        signup.setOnClickListener {
            val email = username.text.toString()
            val password = password.text.toString()
            val confirm = confirm.text.toString()
            if(!email.matches(emailPattern.toRegex())){
                Log.d("test", "email incorrect")
                activity?.runOnUiThread(Runnable{ Toast.makeText(activity, "Enter a proper email", Toast.LENGTH_SHORT).show()})
            } else if(password.isEmpty() || password.length<8){
                Log.d("test", "password incorrect")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Enter a proper password", Toast.LENGTH_SHORT).show()})
            } else if(password != confirm){
                Log.d("test", "password don't match")
                activity?.runOnUiThread(Runnable{Toast.makeText(activity, "Passwords don't match", Toast.LENGTH_SHORT).show()})
            }else{
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
         * @return A new instance of fragment Signup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Signup().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}