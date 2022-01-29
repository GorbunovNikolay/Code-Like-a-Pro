package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb"
        )

        binding.counterLikes.text = post.likesCount.toString()
        binding.likes.setOnClickListener {
            post.lickedByMe = !post.lickedByMe
            post.likesCount = if (post.lickedByMe) ++post.likesCount else --post.likesCount
            val image = if (post.lickedByMe) {
                R.drawable.liked_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }
            binding.counterLikes.text = post.likesCount.toString()
            binding.likes.setImageResource(image)
        }

        binding.counterShare.text = post.shareCount.toString()
        binding.share.setOnClickListener {
            post.shareCount = ++post.shareCount
            binding.counterShare.text = Calc.intToText(post.shareCount)
        }

        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
    }
}

