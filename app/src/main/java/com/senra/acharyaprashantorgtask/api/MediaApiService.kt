package com.senra.acharyaprashantorgtask.api

import com.senra.acharyaprashantorgtask.model.MediaCoverage
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit service interface to define API endpoints for media coverages
interface MediaApiService {

    // Endpoint to fetch a list of media coverages with a specified limit on results
    @GET("v2/content/misc/media-coverages")
    suspend fun getMediaCoverages(@Query("limit") limit: Int): List<MediaCoverage>
}
