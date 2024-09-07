package com.example.healthcarelab.Classes

data class CreateLabTestRequest(
    val name: String,
    val description: String,
    val fees: Double,
    val labId: String
)