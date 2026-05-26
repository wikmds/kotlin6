package com.example.ari.domain

interface PhotoRepository {
    suspend fun getPhotos(): Result<List<Photo>>
}