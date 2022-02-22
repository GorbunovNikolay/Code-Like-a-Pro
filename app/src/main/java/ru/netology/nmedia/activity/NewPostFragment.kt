package ru.netology.nmedia.activity

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val binding = FragmentFeedBinding.inflate(layoutInflater)
        val binding = FragmentNewPostBinding.inflate(layoutInflater)
//        val binding = FragmentPostBinding.inflate(layoutInflater)
//        binding.postContent.content


        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }


        arguments?.textArg?.let {
            binding.content.setText(it)
        }

        binding.content.requestFocus()
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isNotBlank()) {
                viewModel.changeContent(text)
                viewModel.save()
            }
            findNavController().navigateUp()
        }


        return binding.root
    }

    class Contract : ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, NewPostFragment::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                null
            }
        }

    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}