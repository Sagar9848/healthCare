package com.example.healthcarelab.Classes

data class CreateDoctorRequest(
    val name: String,
    val gender: String,
    val hospitalName: String,
    val hospitalAddress: String,
    val pincode: String,
    val emailId: String,
    val mobileNo: String,
    val commission: Double,
    val labId: String
)