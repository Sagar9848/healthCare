package com.example.healthcarelab.screens.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Doctors.AddDoctors.AddDoctorActivity
import com.example.healthcarelab.screens.Doctors.DoctorsList.DoctorsListActivity
import com.example.healthcarelab.screens.PatientHistory.PatientHistoryActivity
import com.example.healthcarelab.screens.PatientScreens.AddPatient.AddPatientsActivity
import com.example.healthcarelab.screens.Tests.AddTest.AddTestsActivity
import com.example.healthcarelab.screens.Tests.TestList.TestListActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val btnPatients: CardView = findViewById(R.id.btnPatients)
        val btnTests: CardView = findViewById(R.id.btnTests)
        val btnDoctors: CardView = findViewById(R.id.btnDoctors)
        val btnPatientHistory: CardView = findViewById(R.id.btnPatientHistory)

        btnPatients.setOnClickListener {
            startActivity(Intent(this, AddPatientsActivity::class.java))
        }

        btnPatientHistory.setOnClickListener {
            startActivity(Intent(this, PatientHistoryActivity::class.java))
        }

        btnTests.setOnClickListener {
            startActivity(Intent(this, TestListActivity::class.java))
        }

        btnDoctors.setOnClickListener {
            startActivity(Intent(this, DoctorsListActivity::class.java))
        }


    }
}