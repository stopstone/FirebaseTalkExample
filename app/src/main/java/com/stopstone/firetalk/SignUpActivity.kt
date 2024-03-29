package com.stopstone.firetalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.stopstone.firetalk.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignUpBinding

    private var name = ""
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        setListener()
    }

    private fun setListener() {
        binding.btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("User")
        with(binding) {
            name = etSignUpNickname.text.toString()
            email = etSignUpEmail.text.toString()
            password = etSignUpPassword.text.toString()
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val user = auth.currentUser
                        val userId = user?.uid
                        val userIdSt = "$userId"

                        userRef.child("users")
                            .child(userId.toString())
                            .setValue(User(name, userIdSt, email))
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("UserId", "$userId")
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "화면 이동 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}