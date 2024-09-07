package com.example.healthcarelab.screens.PatientHistory

import android.net.DnsResolver.Callback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Adapters.PatientHistoryAdapter
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.Classes.Patient
import com.example.healthcarelab.Classes.PatientHistory
import com.example.healthcarelab.Classes.PatientTestWithDetails
import com.example.healthcarelab.Classes.PatientTests
import com.example.healthcarelab.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.lang.reflect.Type
import java.time.LocalDate

class PatientHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var patientAdapter: PatientHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_history)


        recyclerView = findViewById(R.id.recyclerViewPatients)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchPatientTests()
    }

    private fun fetchPatientTests() {
        val labId = "f003bf50-450f-4816-967a-ad1617e6e895"
        val startDate = "2024-08-28"
//        val endDate = "2024-08-30"
        val endDate = LocalDate.now().plusDays(1).toString()

        RetrofitInstance.api.getPatientTests(labId, startDate, endDate)
            .enqueue(object : retrofit2.Callback<List<PatientHistory>> {
                override fun onResponse(
                    call: Call<List<PatientHistory>>,
                    response: Response<List<PatientHistory>>
                ) {
                    if (response.isSuccessful) {
                        val patientTestsList = response.body() ?: emptyList()

                        val detailedPatientTests = mutableListOf<PatientTestWithDetails>()

                        patientTestsList.forEach { patientTest ->
                            fetchDoctorDetails(patientTest.doctorId) { doctorName ->
                                fetchPatientDetails(patientTest.patientId) { patient ->
                                    val patientTestWithDetails = PatientTestWithDetails(
                                        patientTest = patientTest,
                                        doctorName = doctorName,
                                        patientName = patient?.name!!,
                                        gender = patient.gender!!
                                    )

                                    detailedPatientTests.add(patientTestWithDetails)

                                    if (detailedPatientTests.size == patientTestsList.size) {
                                        // Once all details are fetched, update the adapter
                                        patientAdapter = PatientHistoryAdapter(detailedPatientTests)
                                        recyclerView.adapter = patientAdapter
                                    }
                                }
                            }
                        }
                    } else {
                        Log.e("TAG", "Failed to fetch patient tests")
                    }
                }

                override fun onFailure(call: Call<List<PatientHistory>>, t: Throwable) {
                    Log.d("TAG", "onFailure: " + call.request().toString())
                    Toast.makeText(
                        this@PatientHistoryActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun fetchDoctorDetails(doctorId: String, callback: (String) -> Unit) {
        RetrofitInstance.api.getDoctorById(doctorId)
            .enqueue(object : retrofit2.Callback<CreateDoctorResponse> {
                override fun onResponse(
                    call: Call<CreateDoctorResponse>,
                    response: Response<CreateDoctorResponse>
                ) {
                    if (response.isSuccessful) {
                        val doctorName = response.body()?.name ?: "Unknown Doctor"
                        callback(doctorName)
                    } else {
                        callback("Unknown Doctor")
                    }
                }

                override fun onFailure(call: Call<CreateDoctorResponse>, t: Throwable) {
                    callback("Unknown Doctor")
                }
            })
    }

    private fun fetchPatientDetails(patientId: String, callback: (Patient?) -> Unit) {
        RetrofitInstance.api.getPatientById(patientId)
            .enqueue(object : retrofit2.Callback<Patient> {
                override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                    if (response.isSuccessful) {
                        callback(response.body())
                    }
                }

                override fun onFailure(call: Call<Patient>, t: Throwable) {
//                    callback("Unknown Patient")
                }
            })
    }
}