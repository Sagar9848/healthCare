package com.example.healthcarelab.Classes

data class PatientTestWithDetails(
    val patientTest: PatientHistory,
    val doctorName: String,
    val patientName: String,
    val gender: String
)
