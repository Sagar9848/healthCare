package com.example.healthcarelab.Classes

data class PatientTests(
//    val id: String,
    val doctorId: String,
    val patientId: String,
    val labId: String,
    val fees: Int,
    val commission: Int,
    val discount: Int,
    val tests: List<Test>,
    val notes: String

)
