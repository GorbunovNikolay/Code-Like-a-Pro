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
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Calc
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

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.single {
                it.id == postId
            }

            binding.postContent.author.text = post.author
            binding.postContent.published.text = post.published
            binding.postContent.content.text = post.content
            binding.postContent.like.isChecked = post.likedByMe
            binding.postContent.like.text = Calc.intToText(post.likesCount)
            binding.postContent.share.text = Calc.intToText(post.shareCount)
            binding.postContent.view.text = Calc.intToText(5) //заглушка 5
            binding.postContent.playVideoView.isVisible = post.video != null

            binding.postContent.like.setOnClickListener {
                viewModel.likeById(post.id)
            }

            binding.postContent.share.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }

            binding.postContent.playVideoView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

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

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}
