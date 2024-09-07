package com.example.healthcarelab.screens.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.LoginRequest
import com.example.healthcarelab.Classes.LoginResponse
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Dashboard.DashboardActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etMobile: TextInputEditText = findViewById(R.id.etMobile)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)
        val btnSubmit: MaterialButton = findViewById(R.id.btnSubmit)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        loadLoginDetails(etEmail, etPassword)

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(
                    username = email,
                    password = password
                )
                login(loginRequest)
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {
        Log.d("TAG", "Request Login ::: "+RetrofitInstance.api.login(loginRequest).request().toString())

        RetrofitInstance.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    saveLoginDetails(loginRequest.username, loginRequest.password)
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("TAG", "onResponse: "+ response.code())
                    Log.d("TAG", "onResponse: "+ response.message())
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: "+call.request().toString())
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveLoginDetails(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun loadLoginDetails(etEmail: TextInputEditText, etPassword: TextInputEditText) {
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")

        etEmail.setText(savedEmail)
        etPassword.setText(savedPassword)
    }
}