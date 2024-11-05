package com.senra.acharyaprashantorgtask.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCache @Inject constructor(
    @ApplicationContext context: Context
) {
    private val memoryCache: LruCache<String, Bitmap> // In-memory cache for quick access
    private val cacheDir: File = File(context.cacheDir, "images") // Disk cache directory

    init {
        // Allocate 1/8th of available memory for cache
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    // Creates a unique hashed key for storing files on disk
    private fun hashKeyForDisk(key: String): String {
        return try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key.toByteArray())
            val bytes = mDigest.digest()
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            key.hashCode().toString()
        }
    }

    // Adds bitmap to both memory and disk caches
    fun putImageInCache(url: String, bitmap: Bitmap) {
        memoryCache.put(url, bitmap)
        saveBitmapToDisk(url, bitmap)
    }

    // Retrieves bitmap from memory cache first, then disk cache if not found
    fun getImageFromCache(url: String): Bitmap? {
        var bitmap = memoryCache.get(url)
        if (bitmap == null) {
            bitmap = getBitmapFromDisk(url)
            if (bitmap != null) {
                memoryCache.put(url, bitmap) // Update memory cache if found on disk
            }
        }
        return bitmap
    }

    // Writes bitmap to disk for persistent storage
    private fun saveBitmapToDisk(url: String, bitmap: Bitmap) {
        val file = File(cacheDir, hashKeyForDisk(url))
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Loads bitmap from disk if available
    private fun getBitmapFromDisk(url: String): Bitmap? {
        val file = File(cacheDir, hashKeyForDisk(url))
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }
}
