package com.android.developer.prof.reda.astraposts.postFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.astraposts.R
import com.android.developer.prof.reda.astraposts.databinding.FragmentPostBinding
import com.android.developer.prof.reda.astraposts.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val TAG = "PostFragment"

@AndroidEntryPoint
class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private val postAdapter by lazy { PostAdapter() }
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postsRv.adapter = postAdapter

        binding.addPostBtn.setOnClickListener {
            findNavController().navigate(PostFragmentDirections.actionPostFragmentToAddPostFragment( false))
        }

        postAdapter.onClickPost = {
            findNavController().navigate(PostFragmentDirections.actionPostFragmentToDetailPostFragment(it))
        }

        lifecycleScope.launch {
            viewModel.posts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarPosts.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarPosts.visibility = View.GONE
                        postAdapter.submitList(it.data)
                    }
                    is Resource.Failed ->{
                        binding.progressBarPosts.visibility = View.GONE
                        Log.d(TAG, "Fetch posts Error is: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }
}