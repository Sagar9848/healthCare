package com.example.healthcarelab.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.Classes.Patient
import com.example.healthcarelab.Classes.PatientTestWithDetails
import com.example.healthcarelab.Classes.PatientTests
import com.example.healthcarelab.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientHistoryAdapter(private val patientTestWithDetailsList: List<PatientTestWithDetails>) : RecyclerView.Adapter<PatientHistoryAdapter.PatientHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.patient_history_item_layout, parent, false)
        return PatientHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientHistoryViewHolder, position: Int) {
        val patientTestWithDetails = patientTestWithDetailsList[position]
        holder.bind(patientTestWithDetails)
    }

    override fun getItemCount(): Int {
        return patientTestWithDetailsList.size
    }

    class PatientHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        private val tvCreatedOn: TextView = itemView.findViewById(R.id.tvCreatedOn)
        private val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        private val tvFees: TextView = itemView.findViewById(R.id.tvFees)
        private val tvTestDetails: TextView = itemView.findViewById(R.id.tvTestName)
        private val icGenderView: ImageView = itemView.findViewById(R.id.icGenderView)

        fun bind(patientTestWithDetails: PatientTestWithDetails) {
            tvDoctorName.text = "Doctor: ${patientTestWithDetails.doctorName}"
            tvCreatedOn.text = "${formatCreatedOnDate(patientTestWithDetails.patientTest.createdOn)}"
            tvPatientName.text = "Patient: ${patientTestWithDetails.patientName}"
            tvFees.text = "Total Fees: ₹%.2f".format(patientTestWithDetails.patientTest.fees.toDouble())
            tvTestDetails.text = "Tests:  ${patientTestWithDetails.patientTest.tests.joinToString(", ") { it.testName }}"
            Log.d("TAG", "bind: "+patientTestWithDetails.gender)

            if (patientTestWithDetails.gender == "Male"){
                icGenderView.setImageResource(R.drawable.ic_male)
            }else{
                icGenderView.setImageResource(R.drawable.ic_female)
            }

        }


       private fun formatCreatedOnDate(createdOn: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date: Date? = inputFormat.parse(createdOn)
            return date?.let { outputFormat.format(it) } ?: "Unknown Date"
        }
    }
}
