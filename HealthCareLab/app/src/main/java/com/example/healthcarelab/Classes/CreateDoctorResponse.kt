package com.example.healthcarelab.Classes

data class CreateDoctorResponse(
    val id: String,
    val name: String,
    val hospitalName: String,
    val hospitalAddress: String,
    val emailId: String,
    val mobileNo: String,
    val gender: String,
    val pincode: String,
    val commission: Int,
    val labId: String,
    val deleted: Boolean,
    val createdOn: String,
    val updatedOn: String
)