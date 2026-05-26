package com.example.ari.data

import com.example.ari.domain.Photo
import com.squareup.moshi.JsonClass

// Сервер возвращает объект, внутри которого лежит список products
@JsonClass(generateAdapter = true)
data class ProductResponse(
    val products: List<ProductDto>
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val images: List<String>
)

// Превращаем товар в нашу "Фотографию"
fun ProductDto.toDomain(): Photo {
    // Гарантируем, что ссылки используют защищенный протокол HTTPS
    val secureThumbnail = thumbnail.replace("http://", "https://")
    val secureImage = (images.firstOrNull() ?: thumbnail).replace("http://", "https://")

    return Photo(
        id = id.toString(),
        author = title,
        width = 800,
        height = 800,
        url = secureImage,
        downloadUrl = secureImage,
        thumbnailUrl = secureThumbnail
    )
}