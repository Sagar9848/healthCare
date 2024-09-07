package com.example.healthcarelab.Adapters

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.text.intl.Locale
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.R

class TestAdapter(
    private val testList: List<CreateLabTestResponse>,
    private val onTestClick: (String) -> Unit
) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTestName = itemView.findViewById<TextView>(R.id.tvTestName)
        private val tvTestDescription = itemView.findViewById<TextView>(R.id.tvTestDescription)
        private val tvTestFees = itemView.findViewById<TextView>(R.id.tvTestFees)

        fun bind(test: CreateLabTestResponse) {
            tvTestName.text = test.name
            tvTestDescription.text = test.description

            val format = NumberFormat.getCurrencyInstance(java.util.Locale("en", "IN"))
            tvTestFees.text = format.format(test.fees)
            itemView.setOnClickListener {
                onTestClick(test.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_layout, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(testList[position])
    }

    override fun getItemCount(): Int = testList.size
}