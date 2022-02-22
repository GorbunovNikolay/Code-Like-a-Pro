package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val key = "posts"
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {
           if (it.id != id) it else it.copy(
               likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1,
               likedByMe = !it.likedByMe)
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareCount = it.shareCount + 1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter {
            it.id != id
        }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newId = posts.firstOrNull()?.id ?: post.id
            posts = listOf(post.copy(id = newId + 1)) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(posts))
            apply()
        }
    }
}