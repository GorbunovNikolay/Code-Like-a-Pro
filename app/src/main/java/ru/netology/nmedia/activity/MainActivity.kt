package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.ActionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
            object : ActionListener {
                override fun onLikeClick(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClick(post: Post) {
                    viewModel.shareById(post.id)
                }

                override fun onRemoveClick(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onEditClick(post: Post) {
                    viewModel.edit(post)
                }
            }
        )


        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }

            with(binding.edited) {
                requestFocus()
                setText(it.content)
            }
        }

        binding.save.setOnClickListener {
            val text = binding.edited.text?.toString()
            if (text.isNullOrBlank()) {
                Toast.makeText(this, "Content empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.changeContent(text)
            viewModel.save()

            binding.edited.clearFocus()
            binding.edited.setText("")

            AndroidUtils.hideKeyboard(binding.edited)
        }

        binding.edited.setOnFocusChangeListener { _, hasFocus ->
            binding.editMsgGroup.visibility = if (hasFocus) View.VISIBLE else View.GONE
            //Нет фокуса, клавиатура и не нужна
            if (!hasFocus) AndroidUtils.hideKeyboard(binding.edited)
        }

        binding.cancelButton.setOnClickListener {
            binding.edited.text = null
            viewModel.cancelEdit()
            binding.edited.clearFocus()
        }
    }
}
