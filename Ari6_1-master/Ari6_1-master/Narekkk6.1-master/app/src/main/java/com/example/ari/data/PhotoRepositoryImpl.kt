package com.example.ari.data

import com.example.ari.domain.Photo
import com.example.ari.domain.PhotoRepository

class PhotoRepositoryImpl(
    private val api: PicsumApi
) : PhotoRepository {

    override suspend fun getPhotos(): Result<List<Photo>> {
        return try {
            val response = api.getPhotos()
            // Берем список products и маппим каждый элемент
            Result.success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}