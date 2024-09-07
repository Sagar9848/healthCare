package com.example.healthcarelab.Classes

data class Patient(
    val id: String,
    val name: String,
    val dob: String?,
    val address: String,
    val emailId: String,
    val mobileNo: String,
    val gender: String,
    val createdOn: String,
    val createdBy: String,
    val updatedOn: String,
    val updatedBy: String
)