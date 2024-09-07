package com.example.healthcarelab.screens.Tests.AddTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.CreateDoctorRequest
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.Classes.CreateLabTestRequest
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AddTestsActivity : AppCompatActivity() {
    private lateinit var backB: ImageButton
    private lateinit var btnSave: Button
    private lateinit var etTestName: TextInputEditText
    private lateinit var etTestDesc: TextInputEditText
    private lateinit var etTestFees: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tests)

        backB = findViewById(R.id.backB)
        btnSave = findViewById(R.id.btnSave)
        etTestName = findViewById(R.id.etTestName)
        etTestDesc = findViewById(R.id.etTestDesc)
        etTestFees = findViewById(R.id.etTestFees)

        val isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false)
        if (isEditMode) {
            val doctorId = intent.getStringExtra("TEST_ID")
            fetchDoctorDetailsForEdit(doctorId)
            btnSave.text = "Update Details"
        }

        backB.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            if (isEditMode) {
                updateTest()
            } else {
                saveTest()
            }
        }
    }

    private fun saveTest() {
        val testName = etTestName.text.toString()
        val testDesc = etTestDesc.text.toString()
        val testFees = etTestFees.text.toString().toDoubleOrNull()

        if (testName.isNotBlank() && testDesc.isNotBlank() && testFees != null) {
            val request = CreateLabTestRequest(
                name = testName,
                description = testDesc,
                fees = testFees.toDouble(),
                labId = "f003bf50-450f-4816-967a-ad1617e6e895"
            )

            RetrofitInstance.api.createLabTest(request)
                .enqueue(object : retrofit2.Callback<CreateLabTestResponse> {
                    override fun onResponse(
                        call: Call<CreateLabTestResponse>,
                        response: Response<CreateLabTestResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddTestsActivity,
                                "Test saved successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<CreateLabTestResponse>, t: Throwable) {
                        Log.d("TAG", "onFailure: " + call.request().toString())
                        Toast.makeText(
                            this@AddTestsActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTest() {
        val testId = intent.getStringExtra("TEST_ID") ?: return

        val testName = etTestName.text.toString()
        val testDesc = etTestDesc.text.toString()
        val testFees = etTestFees.text.toString()
            .replace(Regex("[^\\d]"), "")
        val request = CreateLabTestRequest(
            testName,
            testDesc,
            testFees.toDouble(),
            "f003bf50-450f-4816-967a-ad1617e6e895"
        )


        RetrofitInstance.api.updateTest(testId, request)
            .enqueue(object : retrofit2.Callback<CreateLabTestResponse> {
                override fun onResponse(
                    call: Call<CreateLabTestResponse>,
                    response: Response<CreateLabTestResponse>
                ) {
                    if (response.isSuccessful) {
                        val updateDoctorResponse = response.body()
                        Toast.makeText(
                            this@AddTestsActivity,
                            "${updateDoctorResponse?.name} Test updated Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddTestsActivity,
                            "Failed to update Test",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CreateLabTestResponse>, t: Throwable) {
                    Toast.makeText(this@AddTestsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun fetchDoctorDetailsForEdit(doctorId: String?) {
        if (doctorId == null) return

        RetrofitInstance.api.getTestById(doctorId)
            .enqueue(object : retrofit2.Callback<CreateLabTestResponse> {
                override fun onResponse(
                    call: Call<CreateLabTestResponse>,
                    response: Response<CreateLabTestResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { doctor ->
                            etTestName.setText(doctor.name)
                            etTestDesc.setText(doctor.description)
                            etTestFees.setText(doctor.fees.toString())
                        }
                    } else {
                        Toast.makeText(
                            this@AddTestsActivity,
                            "Failed to fetch doctor details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CreateLabTestResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddTestsActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}