package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var edtEmail :EditText
    private lateinit var btnLogin: Button
    private lateinit var edtPassword : EditText
    private lateinit var btnSignUp: Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnlogin)
        btnSignUp =findViewById(R.id.btnsignUp)

         mAuth = FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            val intent =Intent(this,SignUp::class.java)
            startActivity(intent)
        }


        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email,password)



        }




    }
    private fun login(email:String ,password :String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener (this){
            if (it.isSuccessful){
                //code for  logging  in user

                val intent =  Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"User does not exist ",Toast.LENGTH_SHORT).show()
            }


        }

    }
}