package com.example.healthcarelab.Classes

data class LoginResponse(
    val userId: String,
    val name: String,
    val labId: String,
    val labName: String,
    val parentLabId: String,
    val token: String,
    val permissions: List<String>,
    val issuedAt: String,
    val expiredAt: String
)
