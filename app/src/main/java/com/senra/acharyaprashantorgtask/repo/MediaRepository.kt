package com.senra.acharyaprashantorgtask.repo

import com.senra.acharyaprashantorgtask.api.MediaApiService
import com.senra.acharyaprashantorgtask.model.MediaCoverage
import com.senra.acharyaprashantorgtask.model.Thumbnail
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    private val mediaApiService: MediaApiService
) {
    // Fetch media coverages from the API with a specified limit
    suspend fun fetchMediaCoverages(limit: Int): Result<List<MediaCoverage>> {
        return try {
            val mediaCoverages = mediaApiService.getMediaCoverages(limit) // API call
            Result.success(mediaCoverages) // Return success result
        } catch (e: IOException) {
            // Handle network errors
            Result.failure(IOException("Network error. Please check your internet connection."))
        } catch (e: HttpException) {
            // Handle server errors
            Result.failure(IOException("Server error. Please try again later."))
        } catch (e: Exception) {
            // Handle unexpected errors
            Result.failure(IOException("An unexpected error occurred."))
        }
    }

    // Construct the full image URL using thumbnail data
    fun constructImageUrl(thumbnail: Thumbnail): String {
        return "${thumbnail.domain}/${thumbnail.basePath}/0/${thumbnail.key}"
    }
}
