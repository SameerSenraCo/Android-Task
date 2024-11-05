package com.senra.acharyaprashantorgtask.model

data class MediaCoverage(
    val id: String,
    val title: String,
    val language: String,
    val thumbnail: Thumbnail,
    val mediaType: Int,
    val coverageURL: String,
    val publishedAt: String,
    val publishedBy: String,
    val backupDetails: BackupDetails,
    val description: String,
    val seoSlugWithId: String
)

data class Thumbnail(
    val id: String,
    val version: Int,
    val domain: String,
    val basePath: String,
    val key: String,
    val qualities: List<Int>,
    val aspectRatio: Float // Change this to Float or Double
)

data class BackupDetails(
    val pdfLink: String,
    val screenshotURL: String
)

