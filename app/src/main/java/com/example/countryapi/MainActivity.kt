package com.example.countryapi


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countryapi.ui.CountryAdapter
import com.example.countryapi.ui.CountryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CountryViewModel
    private lateinit var adapter: CountryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CountryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CountryViewModel::class.java]

        viewModel.countries.observe(this) { countries ->
            adapter.submitList(countries)
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
            } else {
                tvError.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Load countries when activity starts
        viewModel.loadCountries()
    }
}
