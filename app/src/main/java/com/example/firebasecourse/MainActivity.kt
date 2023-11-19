package com.example.firebasecourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnLogOut :Button
    private lateinit var mAuth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogOut = findViewById(R.id.btnLogOut)
        mAuth = FirebaseAuth.getInstance()

        btnLogOut.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(this,"Log Out Successful", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}