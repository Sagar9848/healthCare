package com.example.healthcarelab.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.Classes.MedicalTest
import com.example.healthcarelab.R
import com.google.android.material.checkbox.MaterialCheckBox

class MedicalTestAdapter(
    private val labTests: List<CreateLabTestResponse>,
    private val listener: OnMedicalTestSelectedListener
) : RecyclerView.Adapter<MedicalTestAdapter.MedicalTestViewHolder>() {

    inner class MedicalTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTestName: TextView = itemView.findViewById(R.id.tvTestName)
        private val tvTestFees: TextView = itemView.findViewById(R.id.tvTestPrice)
        private val layoutParent: RelativeLayout = itemView.findViewById(R.id.layoutParent)
        private val cbTestSelected: MaterialCheckBox = itemView.findViewById(R.id.checkBoxSelect)

        fun bind(test: CreateLabTestResponse) {
            tvTestName.text = test.name
            tvTestFees.text = "₹%.2f".format(test.fees.toDouble())

            cbTestSelected.setOnCheckedChangeListener(null)
            cbTestSelected.isChecked = test.isSelected

            cbTestSelected.setOnCheckedChangeListener { _, isChecked ->
                test.isSelected = isChecked
                listener.onTestSelectionChanged(test, isChecked)
                if (isChecked) {
                    layoutParent.setBackgroundResource(R.drawable.selected_item_bg)
                } else {
                    layoutParent.setBackgroundResource(R.drawable.white_rounded_bg)
                }
            }

            if (test.isSelected) {
                layoutParent.setBackgroundResource(R.drawable.selected_item_bg)
            } else {
                layoutParent.setBackgroundResource(R.drawable.white_rounded_bg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalTestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item_layout, parent, false)
        return MedicalTestViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicalTestViewHolder, position: Int) {
        holder.bind(labTests[position])
    }

    override fun getItemCount(): Int = labTests.size

    interface OnMedicalTestSelectedListener {
        fun onTestSelectionChanged(test: CreateLabTestResponse, isSelected: Boolean)
    }
}