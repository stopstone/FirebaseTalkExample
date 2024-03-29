package com.stopstone.firetalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stopstone.firetalk.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        setListeners()
    }


    private fun signInWithEmailAndPassword() {
        if (binding.etSignInEmail.text.toString().isNullOrBlank() ||
            binding.etSignInPassword.text.toString().isNullOrBlank()
        )
            Toast.makeText(this, "아이디 또는 패스워드를 정확히 입력해주세요", Toast.LENGTH_SHORT).show()
        else {
            auth.signInWithEmailAndPassword(
                "${binding.etSignInEmail.text}",
                "${binding.etSignInPassword.text}"
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SignInActivity", auth.currentUser?.email.toString() + "님 로그인 성공")
                        updateUI()
                        finish()
                    } else
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateUI() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        binding.btnSignIn.setOnClickListener {
            signInWithEmailAndPassword()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}

