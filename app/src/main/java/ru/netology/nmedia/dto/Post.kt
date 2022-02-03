package ru.netology.nmedia.dto

data class Post(
   val id: Long = 0L,
   val author: String,
   val published: String,
   val content: String,
   val likedByMe: Boolean = false,
   val likesCount: Int = 0,
   val shareCount: Int = 0
)



