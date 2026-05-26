package com.example.ari.data

import retrofit2.http.GET

interface PicsumApi {
    @GET("products")
    suspend fun getPhotos(): ProductResponse // Теперь получаем ProductResponse
}