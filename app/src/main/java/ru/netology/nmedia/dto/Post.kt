package ru.netology.nmedia.dto

data class Post (
    val id: Long = 0L,
    val likes: Long = 0L,
    val shares: Long = 0L,
    val views: Long = 0L,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val video: String ="",
    val likedByMe: Boolean = false
        )