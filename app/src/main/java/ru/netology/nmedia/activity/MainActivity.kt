package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Calc
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        with(binding) {
            viewModel.data.observe(this@MainActivity) { post ->
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.liked_24 else R.drawable.ic_baseline_favorite_border_24
                )
                //отображаем счетчик лайков
                likesCount.text = post.likesCount.toString()

                //отображаем счетчик поделиться
                shareCount.text = Calc.intToText(post.shareCount)
            }
        }
        binding.likes.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }
    }
}

