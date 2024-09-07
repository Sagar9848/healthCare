package com.example.healthcarelab.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.Classes.CreateDoctorResponse
import com.example.healthcarelab.R


class DoctorAdapter(
    private val doctorList: List<CreateDoctorResponse>,
    private val onDoctorClick: (String) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDoctorImage = itemView.findViewById<ImageView>(R.id.ivDoctorImage)
        private val tvDoctorName = itemView.findViewById<TextView>(R.id.tvDoctorName)
        private val tvMobileNo = itemView.findViewById<TextView>(R.id.tvMobileNo)
        private val tvHospital = itemView.findViewById<TextView>(R.id.tvHospital)

        fun bind(doctor: CreateDoctorResponse) {
            tvDoctorName.text = doctor.name
            tvMobileNo.text = doctor.mobileNo
            tvHospital.text = doctor.hospitalName
            if (doctor.gender == "Male") {
                ivDoctorImage.setImageResource(R.drawable.ic_male_doctor)
            } else {
                ivDoctorImage.setImageResource(R.drawable.ic_female_doctor)
            }
            itemView.setOnClickListener {
                onDoctorClick(doctor.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctorList[position])
    }

    override fun getItemCount(): Int = doctorList.size
}