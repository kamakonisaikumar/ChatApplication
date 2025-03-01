package com.example.chatapplication

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import android.R.attr.password
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.sql.DatabaseMetaData


class SignUp : AppCompatActivity() {

    private lateinit var edtEmail : EditText
    private lateinit var edtName :EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignUp: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        edtName = findViewById(R.id.edt_name)
        edtPassword =findViewById(R.id.edt_password)
        edtEmail = findViewById(R.id.edt_email)
        btnSignUp = findViewById(R.id.btnsignUp)

        btnSignUp.setOnClickListener {

            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password =edtPassword.text.toString()

            signUp(name,email,password)
        }



    }
    private fun signUp(name: String ,email : String ,password :String){

        //login for logging user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home

                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)

                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                  Toast.makeText(this ,"Some Error Occurred ",Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDatabase(name: String ,email: String , uid :String){

       mDbRef = FirebaseDatabase.getInstance().getReference()
       // mDbRef.child("user").child(uid).setValue(User(name,email,uid))
        mDbRef .child("user").child(uid).setValue(User(name,email, uid))
    }

}