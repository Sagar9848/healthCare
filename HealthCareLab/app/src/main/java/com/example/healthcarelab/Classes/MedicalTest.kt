package com.example.healthcarelab.Classes

data class MedicalTest(
    val testId: String,
    val testName: String,
    val testDescription: String,
    val testCategory: String,
    val testFees: Double,
    val testDuration: String,
    var isSelected: Boolean = false
)
