package ru.netology.nmedia

data class Post(
   val id: Long = 0L,
   val author: String,
   val published: String,
   val content: String,
   var lickedByMe: Boolean = false,
   var likesCount: Int = 0,
   var shareCount: Int = 0
)



