package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likesCount: Int = 0,
    val shareCount: Int = 0,
) {
    fun toDto() = Post(id, author, published, content, likedByMe, likesCount, shareCount)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.published, dto.content, dto.likedByMe, dto.likesCount, dto.shareCount)

    }
}

