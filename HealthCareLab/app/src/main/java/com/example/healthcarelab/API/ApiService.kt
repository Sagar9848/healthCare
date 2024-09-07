package com.example.healthcarelab.API

import com.example.healthcarelab.Classes.CreateDoctorRequest
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.Classes.CreateLabTestRequest
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.Classes.CreatePatient
import com.example.healthcarelab.Classes.LoginRequest
import com.example.healthcarelab.Classes.LoginResponse
import com.example.healthcarelab.Classes.Patient
import com.example.healthcarelab.Classes.PatientHistory
import com.example.healthcarelab.Classes.PatientTests
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/doctors")
    fun createDoctor(@Body createDoctorRequest: CreateDoctorRequest): Call<CreateDoctorResponse>

    @GET("/api/v1/doctors")
    fun getAllDoctors(): Call<List<CreateDoctorResponse>>

    @GET("/api/v1/doctors/{id}")
    fun getDoctorById(@Path("id") doctorId: String): Call<CreateDoctorResponse>

    @DELETE("/api/v1/doctors/{id}")
    fun deleteDoctor(@Path("id") doctorId: String): Call<Void>

    @PUT("/api/v1/doctors/{id}")
    fun updateDoctor(
        @Path("id") id: String,
        @Body doctor: CreateDoctorRequest
    ): Call<CreateDoctorResponse>

    @POST("/api/v1/labtests")
    fun createLabTest(@Body createLabTestRequest: CreateLabTestRequest): Call<CreateLabTestResponse>

    @GET("/api/v1/labtests/{id}")
    fun getTestById(@Path("id") testId: String): Call<CreateLabTestResponse>

    @DELETE("/api/v1/labtests/{id}")
    fun deleteTest(@Path("id") testId: String): Call<Void>

    @PUT("/api/v1/labtests/{id}")
    fun updateTest(
        @Path("id") id: String,
        @Body doctor: CreateLabTestRequest
    ): Call<CreateLabTestResponse>

    @GET("/api/v1/labtests")
    fun getAllTests(): Call<List<CreateLabTestResponse>>

    @POST("/api/v1/patients")
    fun createPatient(@Body createPatient: CreatePatient): Call<Patient>

    @GET("/api/v1/patients")
    fun getAllPatients(): Call<List<Patient>>

    @GET("/api/v1/patients/{id}")
    fun getPatientById(@Path("id") patientId: String): Call<Patient>

    @POST("/api/v1/patienttests")
    fun addPatientsTests(@Body patientTests: PatientTests): Call<PatientTests>

    @GET("/api/v1/patienttests")
    fun getPatientTests(
        @Query("labId") labId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Call<List<PatientHistory>>
}
