package com.example.healthcarelab.screens.PatientScreens.AddPatient

import android.content.Intent
import android.net.DnsResolver.Callback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.Classes.CreatePatient
import com.example.healthcarelab.Classes.Patient
import com.example.healthcarelab.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response

class AddPatientsActivity : AppCompatActivity() {

    private lateinit var patientList: List<Patient>
    private lateinit var doctorList: List<CreateDoctorResponse>

    private lateinit var etPatientName: AutoCompleteTextView
    private lateinit var etDob: TextInputEditText
    private lateinit var etDoctorName: AutoCompleteTextView
    private lateinit var etEmailId: TextInputEditText
    private lateinit var etMobileNumber: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etNotes: TextInputEditText
    private lateinit var btnSave: MaterialButton
    val apiService = RetrofitInstance.api

    private lateinit var genderToggleGroup: MaterialButtonToggleGroup
    private lateinit var selectedGender: String

    private var selectedPatientId: String? = null
    private var selectedDoctorId: String? = null
    private var selectedDoctorCom: Int? = null
    private var selectedDoctorFees: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patients)
        etPatientName = findViewById(R.id.etPatientName)
        etDob = findViewById(R.id.etDob)
        etDoctorName = findViewById(R.id.etDoctorName)
        etEmailId = findViewById(R.id.etEmailId)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etAddress = findViewById(R.id.etAddress)
        etNotes = findViewById(R.id.etNotes)
        btnSave = findViewById(R.id.btnSave)

        fetchPatientList()
        fetchDoctorList()
        btnSave.setOnClickListener {
            savePatientDetails()
//            invokeNextScreen()
        }

        genderToggleGroup = findViewById(R.id.genderToggleGroup)
        selectedGender = "Male"

        genderToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnMale -> {
                        selectedGender = "Male"
                        updateButtonColors(R.id.btnMale)
                    }

                    R.id.btnFemale -> {
                        selectedGender = "Female"
                        updateButtonColors(R.id.btnFemale)
                    }

                    R.id.btnOther -> {
                        selectedGender = "Other"
                        updateButtonColors(R.id.btnOther)
                    }
                }
            }
        }
    }

    private fun savePatientDetails() {
        val name = etPatientName.text.toString()
        val dob = etDob.text.toString()
        val address = etAddress.text.toString()
        val emailId = etEmailId.text.toString()
        val mobileNo = etMobileNumber.text.toString()
        val gender = selectedGender

        if (name.isEmpty() || dob.isEmpty() || address.isEmpty() || emailId.isEmpty() || mobileNo.isEmpty() || selectedGender.isEmpty() ) {
            showToast("Please fill all the required fields")
            return
        }
        if (!selectedPatientId.isNullOrEmpty()) {
            showToast("Patient already exists. Proceeding to the next screen.")
            invokeNextScreen()
            return
        }
        val createPatient = CreatePatient(
            name = name,
            dob = dob,
            address = address,
            emailId = emailId,
            mobileNo = mobileNo,
            gender = gender
        )

        apiService.createPatient(createPatient).enqueue(object : retrofit2.Callback<Patient> {
            override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                if (response.isSuccessful) {
                    val createdPatient = response.body()
                    selectedPatientId = createdPatient?.id // Set the patient ID
                    showToast("Patient created successfully!")
                    Log.d("TAG", "Patient created: ${response.body()}")
                    invokeNextScreen()
                } else {
                    showToast("Failed to create patient")
                    Log.e("TAG", "Failed to create patient: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Patient>, t: Throwable) {
                showToast("Error: ${t.message}")
                Log.e("TAG", "API call failed: ${t.message}", t)
            }
        })
    }

    private fun invokeNextScreen() {
        val intent = Intent(this, AddTestsForPatientActivity::class.java).apply {
            putExtra("PATIENT_ID", selectedPatientId)
            putExtra("DOCTOR_ID", selectedDoctorId)
            putExtra("COMMISSION", selectedDoctorCom.toString()) // Example value; replace with actual data
            putExtra("FEES", selectedDoctorFees.toString()) // Example value; replace with actual data
        }
        startActivity(intent)
    }

    private fun updateButtonColors(selectedButtonId: Int) {
        val selectedShapeDrawable = MaterialShapeDrawable().apply {
            fillColor = ContextCompat.getColorStateList(this@AddPatientsActivity, R.color.green_500)
            setStroke(2f, ContextCompat.getColor(this@AddPatientsActivity, R.color.green_500))
            setCornerSize(8f)
        }

        val unselectedShapeDrawable = MaterialShapeDrawable().apply {
            fillColor = ContextCompat.getColorStateList(this@AddPatientsActivity, R.color.white)
            setStroke(2f, ContextCompat.getColor(this@AddPatientsActivity, R.color.green_500))
            setCornerSize(8f)
        }

        findViewById<MaterialButton>(R.id.btnMale).apply {
            background = unselectedShapeDrawable
            setTextColor(ContextCompat.getColor(context, R.color.green_500))
        }
        findViewById<MaterialButton>(R.id.btnFemale).apply {
            background = unselectedShapeDrawable
            setTextColor(ContextCompat.getColor(context, R.color.green_500))
        }
        findViewById<MaterialButton>(R.id.btnOther).apply {
            background = unselectedShapeDrawable
            setTextColor(ContextCompat.getColor(context, R.color.green_500))
        }

        // Apply the selected drawable to the selected button
        findViewById<MaterialButton>(selectedButtonId).apply {
            background = selectedShapeDrawable
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    private fun fetchDoctorList() {

        apiService.getAllDoctors().enqueue(object : retrofit2.Callback<List<CreateDoctorResponse>> {
            override fun onResponse(
                call: Call<List<CreateDoctorResponse>>,
                response: Response<List<CreateDoctorResponse>>
            ) {
                if (response.isSuccessful) {
                    doctorList = response.body() ?: emptyList()
                    setupDoctorNameDropdown(doctorList)
                    Log.d("TAG", "onResponse: " + doctorList.size)
                } else {
                    showToast("Failed to retrieve doctor list")
                }
            }

            override fun onFailure(call: Call<List<CreateDoctorResponse>>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun setupDoctorNameDropdown(doctors: List<CreateDoctorResponse>) {
        val doctorNames = doctors.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, doctorNames)
        etDoctorName.setAdapter(adapter)
        etDoctorName.threshold = 0

        etDoctorName.setOnItemClickListener { _, _, position, _ ->
            val selectedDoctor = doctors[position]
            selectedDoctorId = selectedDoctor.id // Store the selected doctor ID
            selectedDoctorCom = selectedDoctor.commission // Store the selected doctor ID
            showToast("Selected: ${selectedDoctor.name}")
        }
    }

    private fun fetchPatientList() {

        apiService.getAllPatients().enqueue(object : retrofit2.Callback<List<Patient>> {
            override fun onResponse(call: Call<List<Patient>>, response: Response<List<Patient>>) {
                if (response.isSuccessful) {
                    patientList = response.body() ?: emptyList()
                    setupPatientNameDropdown(patientList)
                    Log.d("TAG", "onResponse: " + response.body())
                } else {
                    showToast("Failed to retrieve patient list")
                }
            }

            override fun onFailure(call: Call<List<Patient>>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun setupPatientNameDropdown(patients: List<Patient>) {
        val patientNames = patients.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, patientNames)
        etPatientName.setAdapter(adapter)
        etPatientName.threshold = 0

        etPatientName.setOnItemClickListener { _, _, position, _ ->
            val selectedPatient = patients[position]
            selectedPatientId = selectedPatient.id // Store the selected patient ID
            showToast("Selected: ${selectedPatient.name}")
            attachPatientDetails(selectedPatient)
        }
    }

    private fun attachPatientDetails(patient: Patient) {
        etMobileNumber.text = Editable.Factory.getInstance().newEditable(patient.mobileNo ?: "")
        etEmailId.text = Editable.Factory.getInstance().newEditable(patient.emailId ?: "")
        etDob.text = Editable.Factory.getInstance().newEditable(patient.dob ?: "")
        etAddress.text = Editable.Factory.getInstance().newEditable(patient.address ?: "")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
