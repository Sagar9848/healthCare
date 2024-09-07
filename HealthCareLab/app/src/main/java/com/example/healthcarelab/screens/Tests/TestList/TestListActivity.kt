package com.example.healthcarelab.screens.Tests.TestList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarelab.API.ApiService
import com.example.healthcarelab.API.RetrofitInstance
import com.example.healthcarelab.Adapters.TestAdapter
import com.example.healthcarelab.Classes.CreateLabTestResponse
import com.example.healthcarelab.R
import com.example.healthcarelab.screens.Tests.AddTest.AddTestsActivity
import com.example.healthcarelab.screens.Tests.TestDetails.TestDetailsActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TestListActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TestAdapter
    private lateinit var btnAddTests: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_list)

        recyclerView = findViewById(R.id.recyclerViewTests)
        btnAddTests = findViewById(R.id.btnAddTests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchTests()

        btnAddTests.setOnClickListener {
            startActivity(Intent(this, AddTestsActivity::class.java))
        }
    }

    override fun onResume() {
        fetchTests()
        super.onResume()
    }

    private fun fetchTests() {
        RetrofitInstance.api.getAllTests().enqueue(object : Callback<List<CreateLabTestResponse>> {
            override fun onResponse(
                call: Call<List<CreateLabTestResponse>>,
                response: Response<List<CreateLabTestResponse>>
            ) {
                if (response.isSuccessful) {
                    val tests = response.body()
                    if (tests != null) {
                        adapter = TestAdapter(tests) { testId ->
                            val intent = Intent(this@TestListActivity, TestDetailsActivity::class.java)
                            intent.putExtra("TEST_ID", testId)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CreateLabTestResponse>>, t: Throwable) {
                Toast.makeText(this@TestListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}