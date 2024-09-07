package com.example.healthcarelab.screens.Doctors.DoctorDetails

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.ui.window.Dialog
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Doctors.AddDoctors.AddDoctorActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorsDetailsActivity : AppCompatActivity() {

    private lateinit var tvDoctorName: TextView
    private lateinit var tvEmailId: TextView
    private lateinit var tvMobileNumber: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvHospitalName: TextView
    private lateinit var tvCommissionPercentage: TextView
    private lateinit var tvCreatedBy: TextView
    private lateinit var btnDelete: AppCompatButton
    private lateinit var btnEdit: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_details)

        tvDoctorName = findViewById(R.id.tvDoctorName)
        tvEmailId = findViewById(R.id.tvEmailId)
        tvMobileNumber = findViewById(R.id.tvMobileNumber)
        tvGender = findViewById(R.id.tvGender)
        tvHospitalName = findViewById(R.id.tvHospitalName)
        tvCommissionPercentage = findViewById(R.id.tvCommissionPercentage)
        tvCreatedBy = findViewById(R.id.tvCreatedBy)
        btnDelete = findViewById(R.id.btnDelete)
        btnEdit = findViewById(R.id.btnEdit)


        val doctorId = intent.getStringExtra("DOCTOR_ID") ?: return

        fetchDoctorDetails(doctorId)

        btnDelete.setOnClickListener {
            showConfirmDeleteDialog(doctorId)
        }

        btnEdit.setOnClickListener { // Add this block
            val intent = Intent(this, AddDoctorActivity::class.java)
            intent.putExtra("DOCTOR_ID", doctorId)
            intent.putExtra("IS_EDIT_MODE", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val doctorId = intent.getStringExtra("DOCTOR_ID") ?: return
        fetchDoctorDetails(doctorId)

    }
    private fun showConfirmDeleteDialog(doctorId: String) {
        val dialog = Dialog(this, R.style.TransparentDialog)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        dialog.setContentView(dialogView)

        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<AppCompatButton>(R.id.btnConfirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            deleteDoctor(doctorId)
        }

        dialog.show()
    }

    private fun deleteDoctor(doctorId: String) {
        RetrofitInstance.api.deleteDoctor(doctorId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("TAG", "onResponse: "+response.code())
                Log.d("TAG", "onResponse: "+response.message())
                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorsDetailsActivity, "Doctor deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DoctorsDetailsActivity, "Failed to delete doctor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@DoctorsDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchDoctorDetails(doctorId: String) {
        RetrofitInstance.api.getDoctorById(doctorId).enqueue(object : Callback<CreateDoctorResponse> {
            override fun onResponse(call: Call<CreateDoctorResponse>, response: Response<CreateDoctorResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { doctor ->
                        tvDoctorName.text ="Name: ${doctor.name}"
                        tvEmailId.text = "Email: ${doctor.emailId}"
                        tvMobileNumber.text = "Mobile No: ${doctor.mobileNo}"
                        tvGender.text = "Gender: ${doctor.gender}"
                        tvHospitalName.text = "Hospital Name: ${doctor.hospitalName}"
                        tvCommissionPercentage.text = "Commission Percentage: ${doctor.commission}%"
                        tvCreatedBy.text = "Created By: ${doctor.createdOn}"
                    }
                } else {
                    Toast.makeText(this@DoctorsDetailsActivity, "Failed to fetch doctor details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateDoctorResponse>, t: Throwable) {
                Toast.makeText(this@DoctorsDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}