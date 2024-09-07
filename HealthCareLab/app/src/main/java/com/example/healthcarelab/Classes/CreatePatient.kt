package com.example.healthcarelab.Classes

data class CreatePatient(
    val name: String,
    val dob: String,
    val address: String,
    val emailId: String,
    val mobileNo: String,
    val gender: String
)
