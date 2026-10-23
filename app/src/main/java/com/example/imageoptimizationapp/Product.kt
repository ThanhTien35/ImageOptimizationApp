package com.example.imageoptimizationapp

data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
)

fun getProducts(): List<Product> {
    return List(100) { index ->
        Product(
            id = index + 1,
            name = "Product ${index + 1}",
            imageUrl = "https://picsum.photos/seed/${index + 1}/800/600",
            description = "Description for Product ${index + 1}"
        )
    }
}