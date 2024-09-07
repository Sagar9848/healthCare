package com.example.healthcarelab.screens.Doctors.DoctorsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.API.ApiService
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Adapters.DoctorAdapter
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Doctors.AddDoctors.AddDoctorActivity
import com.example.healthcarelab.screens.Doctors.DoctorDetails.DoctorsDetailsActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DoctorsListActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DoctorAdapter
    private lateinit var btnAddDoctors: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_list)

        recyclerView = findViewById(R.id.recyclerViewDoctors)
        btnAddDoctors = findViewById(R.id.btnAddDoctors)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchDoctors()

        btnAddDoctors.setOnClickListener{
            startActivity(Intent(this, AddDoctorActivity::class.java))
        }
    }

    override fun onResume() {
        fetchDoctors()
        super.onResume()
    }
    private fun fetchDoctors() {
        RetrofitInstance.api.getAllDoctors().enqueue(object : Callback<List<CreateDoctorResponse>> {
            override fun onResponse(
                call: Call<List<CreateDoctorResponse>>,
                response: Response<List<CreateDoctorResponse>>
            ) {
                Log.d("TAG", "getAllDoctors onResponse: "+ response.code() + " --- " + response.body())
                if (response.isSuccessful) {
                    val doctors = response.body()
                    if (doctors != null) {
                        adapter = DoctorAdapter(doctors) { doctorId ->
                            val intent = Intent(this@DoctorsListActivity, DoctorsDetailsActivity::class.java)
                            intent.putExtra("DOCTOR_ID", doctorId)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CreateDoctorResponse>>, t: Throwable) {
                Toast.makeText(this@DoctorsListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}