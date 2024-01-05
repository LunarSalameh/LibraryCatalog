package com.example.librarycatalog

data class Book(
    val id: Int,
    val name: String,
    val description: String,
    val publishDate: String,
    val author: String,
    val imageLink: String
)