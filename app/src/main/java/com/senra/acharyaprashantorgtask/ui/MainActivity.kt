package com.senra.acharyaprashantorgtask.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.senra.acharyaprashantorgtask.databinding.ActivityMainBinding
import com.senra.acharyaprashantorgtask.viewmodel.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mediaAdapter: MediaAdapter // Dependency injection for the MediaAdapter
    private val mediaViewModel: MediaViewModel by viewModels() // ViewModel for media data
    private lateinit var binding: ActivityMainBinding // View binding for the layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the layout
        setContentView(binding.root) // Set the content view

        setupRecyclerView() // Initialize RecyclerView
        observeMediaCoverages() // Observe media coverages from ViewModel
        observeErrorMessages() // Observe error messages from ViewModel
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3) // Set layout manager for grid
            adapter = mediaAdapter // Set the adapter for RecyclerView
            setHasFixedSize(true) // Improve performance by avoiding layout changes
            setItemViewCacheSize(20) // Set cache size for view recycling
        }
    }

    private fun observeMediaCoverages() {
        mediaViewModel.mediaCoverages.observe(this) { mediaCoverages ->
            mediaAdapter.submitList(mediaCoverages) // Update the adapter with new media coverages
        }
    }

    private fun observeErrorMessages() {
        mediaViewModel.errorMessage.observe(this) { errorMessage ->
            binding.errorTextView.visibility = View.VISIBLE // Show error message
            binding.errorTextView.text = errorMessage // Set the error message text
        }
    }
}
