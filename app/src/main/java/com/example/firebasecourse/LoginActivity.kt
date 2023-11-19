package com.example.firebasecourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmailId : EditText
    private lateinit var edtEmailPassword : EditText

    private lateinit var btnLogIn : Button
    private lateinit var btnSignUp : Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        edtEmailId = findViewById(R.id.edtEmailId)
        edtEmailPassword = findViewById(R.id.edtEmailPassword)

        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUp = findViewById(R.id.btnSignUp)

        mAuth = FirebaseAuth.getInstance()



        btnSignUp.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            val emailId = edtEmailId.text.toString()
            val password = edtEmailPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO){
                signUp(mAuth,emailId, password)
            }
        }

        btnLogIn.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            val emailId = edtEmailId.text.toString()
            val password = edtEmailPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO){
                signIn(mAuth,emailId, password)
                Toast.makeText(this@LoginActivity, "$emailId + $password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun signUp(
        firebaseAuth: FirebaseAuth,
        emailId : String,
        password: String) :AuthResult? {

        return try{
            val result = firebaseAuth.createUserWithEmailAndPassword(emailId, password)
                .await()

            updateUI(result.user)
            result
        } catch (e :Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AuthResult", "${e.message}")
                progressBar.visibility = View.GONE
            }
            null
        }
    }

    suspend fun signIn(
        firebaseAuth: FirebaseAuth,
        emailId : String,
        password: String) :AuthResult? {

        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(emailId, password)
                .await()

            updateUI(result.user)
            result
        } catch (e :Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AuthResult", "${e.message}")
                progressBar.visibility = View.GONE
            }
            null
        }
    }

    private suspend fun updateUI(firebaseUser: FirebaseUser?){
        Toast.makeText(this, "${firebaseUser?.email}", Toast.LENGTH_SHORT).show()
        Log.d("AuthResult", "${firebaseUser?.email}")

        withContext(Dispatchers.Main){
            progressBar.visibility = View.GONE
            Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("LifeCycleAware", "onStart")
        val currentUser = mAuth.currentUser
        if(currentUser !=null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}