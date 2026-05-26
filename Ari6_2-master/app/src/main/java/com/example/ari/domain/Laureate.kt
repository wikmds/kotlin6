package com.example.ari.domain

data class Laureate(
    val id: String,
    val awardYear: String,
    val category: String,
    val fullName: String,
    val motivation: String,
    val birthCountry: String
) {
    // с инициалами, чтобы картинка красиво отображалась в UI.
    val photoUrl: String
        get() = "https://ui-avatars.com/api/?name=${fullName.replace(" ", "+")}&size=400&background=random"
}
