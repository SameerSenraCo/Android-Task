package com.senra.acharyaprashantorgtask.networking

import com.senra.acharyaprashantorgtask.api.MediaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Module for providing network-related dependencies
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provides an OkHttpClient instance with specified timeout settings
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Set read timeout
            .build()
    }

    // Provides a Retrofit instance configured with the base URL and OkHttpClient
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://acharyaprashant.org/api/") // Base API URL
            .client(okHttpClient) // Attach OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
            .build()
    }

    // Provides an instance of MediaApiService for making API calls
    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): MediaApiService {
        return retrofit.create(MediaApiService::class.java) // Create MediaApiService from Retrofit
    }
}
