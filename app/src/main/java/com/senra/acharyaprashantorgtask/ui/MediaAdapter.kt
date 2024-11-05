package com.senra.acharyaprashantorgtask.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.senra.acharyaprashantorgtask.R
import com.senra.acharyaprashantorgtask.cache.ImageCache
import com.senra.acharyaprashantorgtask.databinding.ItemImageBinding
import com.senra.acharyaprashantorgtask.model.MediaCoverage
import com.senra.acharyaprashantorgtask.repo.MediaRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@ActivityScoped
class MediaAdapter @Inject constructor(
    private val repository: MediaRepository,
    private val imageCache: ImageCache
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // AsyncListDiffer to manage the list updates efficiently
    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<MediaCoverage>() {
        override fun areItemsTheSame(oldItem: MediaCoverage, newItem: MediaCoverage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaCoverage, newItem: MediaCoverage): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(differ.currentList[position]) // Bind the current media item
    }

    override fun onViewRecycled(holder: MediaViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelImageLoadJob() // Cancel any ongoing image load
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MediaViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var imageLoadJob: Job? = null

        fun bind(media: MediaCoverage) {
            val imageUrl = repository.constructImageUrl(media.thumbnail)
            binding.apply {
                imageView.setImageBitmap(null) // Clear the previous image
                placeholderText.visibility = View.VISIBLE // Show placeholder

                cancelImageLoadJob() // Cancel any previous job

                imageLoadJob = adapterScope.launch(Dispatchers.IO) {
                    var bitmap = imageCache.getImageFromCache(imageUrl) // Try to get image from cache
                    if (bitmap == null) {
                        try {
                            // Load image from network if not cached
                            val inputStream = java.net.URL(imageUrl).openStream()
                            bitmap = BitmapFactory.decodeStream(inputStream)
                            if (bitmap != null) {
                                imageCache.putImageInCache(imageUrl, bitmap) // Cache the loaded image
                            }
                        } catch (e: IOException) {
                            // Handle network error
                            withContext(Dispatchers.Main) {
                                placeholderText.text = root.context.getString(R.string.network_error)
                            }
                        } catch (e: Exception) {
                            // Handle any other errors
                            withContext(Dispatchers.Main) {
                                placeholderText.text = root.context.getString(R.string.unknown_error)
                            }
                        }
                    }

                    // Update UI on the main thread
                    withContext(Dispatchers.Main) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap) // Set the loaded image
                            placeholderText.visibility = View.GONE // Hide placeholder
                        } else {
                            placeholderText.text = root.context.getString(R.string.error_text) // Show error message
                        }
                    }
                }
            }
        }

        fun cancelImageLoadJob() {
            imageLoadJob?.cancel() // Cancel ongoing image loading job
        }
    }

    fun submitList(newMediaList: List<MediaCoverage>) {
        differ.submitList(newMediaList) // Update the list with new data
    }
}
