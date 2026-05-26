package com.example.ari.domain

class GetPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(): Result<List<Photo>> {
        return repository.getPhotos()
    }
}