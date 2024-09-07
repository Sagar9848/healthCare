package com.example.healthcarelab.screens.PatientScreens.AddPatient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Adapters.MedicalTestAdapter
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.Classes.PatientTests
import com.example.healthcarelab.Classes.Test
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Dashboard.DashboardActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTestsForPatientActivity : AppCompatActivity(),
    MedicalTestAdapter.OnMedicalTestSelectedListener {
    private lateinit var recyclerViewTests: RecyclerView
    private lateinit var medicalTestAdapter: MedicalTestAdapter
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnSave: Button
    private lateinit var labTests: List<CreateLabTestResponse>

    private lateinit var patientId: String
    private lateinit var doctorId: String
    private lateinit var doctorCom: String
    private lateinit var doctorFees: String
    private val labId = "f003bf50-450f-4816-967a-ad1617e6e895" // Replace with actual lab ID
    private val commission = 200 // Replace with actual commission value if needed
    private val discount = 0 // Replace with actual discount value if needed
    private val notes = "Follow up in two weeks" // Replace with actual notes if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tests_for_patient)

        recyclerViewTests = findViewById(R.id.recyclerViewTests)
        recyclerViewTests.layoutManager = LinearLayoutManager(this)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        btnSave = findViewById(R.id.btnSave)
        patientId = intent.getStringExtra("PATIENT_ID") ?: ""
        doctorId = intent.getStringExtra("DOCTOR_ID") ?: ""
        doctorCom = intent.getStringExtra("COMMISSION") ?: ""
        doctorFees = intent.getStringExtra("FEES") ?: ""

        fetchTests()

        btnSave.setOnClickListener {
            saveSelectedTests()
        }
    }

    override fun onTestSelectionChanged(test: CreateLabTestResponse, isSelected: Boolean) {
        test.isSelected = isSelected
        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        val selectedTests = labTests.filter { it.isSelected }
        val totalAmount = selectedTests.sumOf { it.fees }
        Log.d("TAG", "updateTotalAmount: " + "Total Amount: ₹%.2f".format(totalAmount.toDouble()))
        tvTotalAmount.text = "Total Amount: ₹%.2f".format(totalAmount.toDouble())
    }

    private fun fetchTests() {
        RetrofitInstance.api.getAllTests().enqueue(object : Callback<List<CreateLabTestResponse>> {
            override fun onResponse(
                call: Call<List<CreateLabTestResponse>>,
                response: Response<List<CreateLabTestResponse>>
            ) {
                if (response.isSuccessful) {
                    labTests = response.body() ?: emptyList()
                    for (test in labTests) {
                        Log.d("TAG", "onResponse: " + test.name + test.fees)
                    }
                    medicalTestAdapter =
                        MedicalTestAdapter(labTests, this@AddTestsForPatientActivity)
                    recyclerViewTests.adapter = medicalTestAdapter
                } else {
                    showToast("Failed to load tests")
                }
            }

            override fun onFailure(call: Call<List<CreateLabTestResponse>>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun saveSelectedTests() {
        val selectedPatientId = patientId // Replace with actual patient ID
        val selectedDoctorId = doctorId  // Replace with actual doctor ID
        val labId = "f003bf50-450f-4816-967a-ad1617e6e895"  // Replace with actual lab ID
        val fees = labTests.filter { it.isSelected }.sumOf { it.fees }
        val commission = doctorCom.toInt() // Replace with actual commission value if needed
        val discount = doctorFees // Replace with actual discount value if needed

        val tests = labTests.filter { it.isSelected }.map {
            Test(
                testId = it.id,
                testName = it.name,
                fees = it.fees
            )
        }

        // Create the PatientTests object
        val patientTests = PatientTests(
//            id = "123e4567-e89b-12d3-a456-426614174006", // Replace with actual ID if needed
            doctorId = selectedDoctorId,
            patientId = selectedPatientId,
            labId = labId,
            fees = fees,
            commission = commission.toInt(),
            0,
            tests = tests,
            notes = notes
        )

        RetrofitInstance.api.addPatientsTests(patientTests)
            .enqueue(object : Callback<PatientTests> {
                override fun onResponse(
                    call: Call<PatientTests>,
                    response: Response<PatientTests>) {
                    if (response.isSuccessful) {
                        showToast("Tests saved successfully")
                        finish()
                        invokeNextScreen()
                    } else {
                        showToast("Failed to save tests")
                    }
                }

                override fun onFailure(call: Call<PatientTests>, t: Throwable) {
                    showToast("Error: ${t.message}")
                }
            })
    }
    private fun invokeNextScreen() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
