package com.example.healthcarelab.screens.Tests.TestDetails

import android.app.Dialog
import android.content.Intent
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Tests.AddTest.AddTestsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestDetailsActivity : AppCompatActivity() {
    private lateinit var tvTestName: TextView
    private lateinit var tvTestDescription: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnDelete: AppCompatButton
    private lateinit var btnEdit: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_details)

       tvTestName = findViewById(R.id.tvTestName)
       tvTestDescription = findViewById(R.id.tvTestDescription)
       tvPrice = findViewById(R.id.tvPrice)
        btnDelete = findViewById(R.id.btnDelete)
        btnEdit = findViewById(R.id.btnEdit)

        val testId = intent.getStringExtra("TEST_ID") ?: return

        fetchTestDetails(testId)

        btnDelete.setOnClickListener {
            showConfirmDeleteDialog(testId)
        }

        btnEdit.setOnClickListener { // Add this block
            val intent = Intent(this, AddTestsActivity::class.java)
            intent.putExtra("TEST_ID", testId)
            intent.putExtra("IS_EDIT_MODE", true)
            startActivity(intent)
        }
    }  override fun onResume() {
        super.onResume()
        val testId = intent.getStringExtra("TEST_ID") ?: return
        fetchTestDetails(testId)

    }
    private fun showConfirmDeleteDialog(testId: String) {
        val dialog = Dialog(this, R.style.TransparentDialog)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        dialog.setContentView(dialogView)

        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val tvDelete = dialogView.findViewById<TextView>(R.id.tvDelete)
        val tvConfirmMessage = dialogView.findViewById<TextView>(R.id.tvConfirmMessage)
        val btnConfirm = dialogView.findViewById<AppCompatButton>(R.id.btnConfirm)
        tvDelete.text = "Delete Test"
        tvConfirmMessage.text = "Are you sure you want to delete this Test?"
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            deleteTest(testId)
        }

        dialog.show()
    }

    private fun deleteTest(testId: String) {
        RetrofitInstance.api.deleteTest(testId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("TAG", "onResponse: "+response.code())
                Log.d("TAG", "onResponse: "+response.message())
                if (response.isSuccessful) {
                    Toast.makeText(this@TestDetailsActivity, "Test deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@TestDetailsActivity, "Failed to delete Test", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@TestDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchTestDetails(testId: String) {
        RetrofitInstance.api.getTestById(testId).enqueue(object :
            Callback<CreateLabTestResponse> {
            override fun onResponse(call: Call<CreateLabTestResponse>, response: Response<CreateLabTestResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { test ->
                        tvTestName.text ="Test: ${test.name}"
                        tvTestDescription.text = test.description
                        val format = NumberFormat.getCurrencyInstance(java.util.Locale("en", "IN"))
                        tvPrice.text = "Fees: ${format.format(test.fees)}"
                    }
                } else {
                    Toast.makeText(this@TestDetailsActivity, "Failed to fetch test details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateLabTestResponse>, t: Throwable) {
                Toast.makeText(this@TestDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}