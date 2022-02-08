package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Calc
import ru.netology.nmedia.dto.Post

interface ActionListener {
    fun onLikeClick(post: Post)
    fun onShareClick(post: Post)
    fun onRemoveClick(post: Post)
    fun onEditClick(post: Post)
}
//typealias OnLikeListener = (post: Post) -> Unit
//typealias OnShareListener = (post: Post) -> Unit
//typealias OnRemoveListener = (post: Post) -> Unit

class PostsAdapter(
    private val actionListener: ActionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, actionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val actionListener: ActionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            //отображаем счетчик лайков
            likesCount.text = Calc.intToText(post.likesCount)
            viewCount.text = Calc.intToText(5) //заглушка 5
            //отображаем счетчик поделиться
            shareCount.text = Calc.intToText(post.shareCount)
            like.setOnClickListener {
                actionListener.onLikeClick(post)
            }
            share.setOnClickListener {
                actionListener.onShareClick(post)
            }
            menu.setOnClickListener {
                PopupMenu(binding.root.context, binding.menu).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.remove -> {
                                actionListener.onRemoveClick(post)
                                true
                            }
                            R.id.edit -> {
                                actionListener.onEditClick(post)
                                true
                            }
                            else -> false
                        }

                    }
                }
                    .show()
            }
        }
    }
}


class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}