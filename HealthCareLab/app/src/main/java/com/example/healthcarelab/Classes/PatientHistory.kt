package com.example.healthcarelab.Classes

data class PatientHistory(val id: String,
val doctorId: String,
val patientId: String,
val labId: String,
val fees: Int,
val commission: Int,
val discount: Int,
val tests: List<Test>,
val notes: String,
val createdOn: String,
val createdBy: String,
val updatedOn: String,
val updatedBy: String
)