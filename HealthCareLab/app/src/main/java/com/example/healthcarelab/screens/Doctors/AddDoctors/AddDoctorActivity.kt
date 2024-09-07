package com.example.healthcarelab.screens.Doctors.AddDoctors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.CreateDoctorRequest
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AddDoctorActivity : AppCompatActivity() {

    private lateinit var etDoctorName: TextInputEditText
    private lateinit var etEmailId: TextInputEditText
    private lateinit var etHospitalName: TextInputEditText
    private lateinit var etHospitalAddress: TextInputEditText
    private lateinit var etPinCode: TextInputEditText
    private lateinit var etMobileNumber: TextInputEditText
    private lateinit var etCommissionPercentage: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var backB: ImageButton
    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var rbOther: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)

        etDoctorName = findViewById(R.id.etDoctorName)
        etEmailId = findViewById(R.id.etEmailId)
        etHospitalName = findViewById(R.id.etHospitalName)
        etHospitalAddress = findViewById(R.id.etHospitalAddress)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPinCode = findViewById(R.id.etPinCode)
        etCommissionPercentage = findViewById(R.id.etCommissionPercentage)
        btnSave = findViewById(R.id.btnSave)
        backB = findViewById(R.id.backB)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        rbOther = findViewById(R.id.rbOther)


        val isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false)
        if (isEditMode) {
            val doctorId = intent.getStringExtra("DOCTOR_ID")
            fetchDoctorDetailsForEdit(doctorId)
            btnSave.text = "Update Details"
        }

        btnSave.setOnClickListener {
            if (isEditMode) {
                updateDoctor()
            } else {
                saveDoctor()
            }
        }

        backB.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchDoctorDetailsForEdit(doctorId: String?) {
        if (doctorId == null) return

        RetrofitInstance.api.getDoctorById(doctorId)
            .enqueue(object : retrofit2.Callback<CreateDoctorResponse> {
                override fun onResponse(
                    call: Call<CreateDoctorResponse>,
                    response: Response<CreateDoctorResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { doctor ->
                            etDoctorName.setText(doctor.name)
                            etEmailId.setText(doctor.emailId)
                            etHospitalName.setText(doctor.hospitalName)
                            etMobileNumber.setText(doctor.mobileNo)
                            etCommissionPercentage.setText(doctor.commission.toString())

                            when (doctor.gender) {
                                "Male" -> rgGender.check(R.id.rbMale)
                                "Female" -> rgGender.check(R.id.rbFemale)
                                "Other" -> rgGender.check(R.id.rbOther)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@AddDoctorActivity,
                            "Failed to fetch doctor details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CreateDoctorResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddDoctorActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveDoctor() {
        val doctorName = etDoctorName.text.toString()
        val emailId = etEmailId.text.toString()
        val hospitalName = etHospitalName.text.toString()
        val hospitalAddress = etHospitalAddress.text.toString()
        val pinCode = etPinCode.text.toString()
        val mobileNumber = etMobileNumber.text.toString()
        val commissionPercentage = etCommissionPercentage.text.toString().toIntOrNull() ?: 0
        val gender = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> "Male"
            R.id.rbFemale -> "Female"
            R.id.rbOther -> "Other"
            else -> ""
        }

        val createDoctorRequest = CreateDoctorRequest(
            name = doctorName,
            gender = gender,
            hospitalName = hospitalName,
            hospitalAddress = hospitalAddress,
            pincode = pinCode,
            emailId = emailId,
            mobileNo = mobileNumber,
            commission = commissionPercentage.toDouble(),
            labId = "f003bf50-450f-4816-967a-ad1617e6e895"
        )

        RetrofitInstance.api.createDoctor(createDoctorRequest)
            .enqueue(object : retrofit2.Callback<CreateDoctorResponse> {
                override fun onResponse(
                    call: Call<CreateDoctorResponse>,
                    response: Response<CreateDoctorResponse>
                ) {
                    if (response.isSuccessful) {
                        val createDoctorResponse = response.body()
                        Toast.makeText(
                            this@AddDoctorActivity,
                            " * ${createDoctorResponse?.name} Doctor created Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddDoctorActivity,
                            "Failed to create doctor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CreateDoctorResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddDoctorActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun updateDoctor() {
        val doctorName = etDoctorName.text.toString()
        val emailId = etEmailId.text.toString()
        val hospitalName = etHospitalName.text.toString()
        val hospitalAddress = etHospitalAddress.text.toString()
        val pinCode = etPinCode.text.toString()
        val mobileNumber = etMobileNumber.text.toString()
        val commissionPercentage = etCommissionPercentage.text.toString().toIntOrNull() ?: 0
        val gender = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> "Male"
            R.id.rbFemale -> "Female"
            R.id.rbOther -> "Other"
            else -> ""
        }

        val doctorId = intent.getStringExtra("DOCTOR_ID") ?: return

        val updateDoctorRequest = CreateDoctorRequest(
            name = doctorName,
            gender = gender,
            hospitalName = hospitalName,
            hospitalAddress = hospitalAddress,
            pincode = pinCode,
            emailId = emailId,
            mobileNo = mobileNumber,
            commission = commissionPercentage.toDouble(),
            labId = "f003bf50-450f-4816-967a-ad1617e6e895"
        )

        RetrofitInstance.api.updateDoctor(doctorId, updateDoctorRequest)
            .enqueue(object : retrofit2.Callback<CreateDoctorResponse> {
                override fun onResponse(
                    call: Call<CreateDoctorResponse>,
                    response: Response<CreateDoctorResponse>
                ) {
                    if (response.isSuccessful) {
                        val updateDoctorResponse = response.body()
                        Toast.makeText(
                            this@AddDoctorActivity,
                            "${updateDoctorResponse?.name} Doctor updated Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddDoctorActivity,
                            "Failed to update doctor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CreateDoctorResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddDoctorActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}