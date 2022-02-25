package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg
import ru.netology.nmedia.adapter.ActionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Calc
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class FragmentPost : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by viewModels(::requireParentFragment)
        val postId = arguments?.textArg?.toLong()
        //  binding.postContent.content для доступа из родителя

        if (postId != null) {
            viewModel.getPostById(postId).let { post ->
                binding.apply {
                    binding.postContent.author.text = post.author
                    binding.postContent.published.text = post.published
                    binding.postContent.content.text = post.content
                    binding.postContent.like.isChecked = post.likedByMe
                    binding.postContent.like.text = Calc.intToText(post.likesCount)
                    binding.postContent.share.text = Calc.intToText(post.shareCount)
                    binding.postContent.view.text = Calc.intToText(5) //заглушка 5
                    binding.postContent.playVideoView.isVisible = post.video != null

                    binding.postContent.menu.setOnClickListener {
                        PopupMenu(binding.root.context, binding.postContent.menu).apply {
                            inflate(R.menu.post_menu)
                            setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.remove -> {
                                        viewModel.removeById(post.id)
                                        findNavController().navigateUp()
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.edit(post)
                                        findNavController().navigate(R.id.action_fragmentPost_to_newPostFragment,
                                            Bundle().apply { textArg = post.content })
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

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}
