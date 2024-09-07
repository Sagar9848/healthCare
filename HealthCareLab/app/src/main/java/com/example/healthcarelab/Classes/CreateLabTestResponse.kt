package com.example.healthcarelab.Classes

data class CreateLabTestResponse(
    val id: String,
    val name: String,
    val description: String,
    val fees: Int,
    val labId: String,
    val createdOn: String,
    val createdBy: String,
    val updatedOn: String,
    val updatedBy: String,
    var isSelected: Boolean = false
)