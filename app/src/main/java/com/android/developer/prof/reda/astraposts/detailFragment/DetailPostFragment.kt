package com.android.developer.prof.reda.astraposts.detailFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.astraposts.DataBinderMapperImpl
import com.android.developer.prof.reda.astraposts.PostModel
import com.android.developer.prof.reda.astraposts.R
import com.android.developer.prof.reda.astraposts.SharedViewModel
import com.android.developer.prof.reda.astraposts.addPostFragment.AddPostFragment
import com.android.developer.prof.reda.astraposts.databinding.FragmentDetailPostBinding
import com.bumptech.glide.Glide

class DetailPostFragment : Fragment() {

    private lateinit var binding: FragmentDetailPostBinding
    private lateinit var post: PostModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.main_nav)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = DetailPostFragmentArgs.fromBundle(requireArguments()).post
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail_post,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireActivity())
            .load(post.post_image)
            .into(binding.postPic)

        binding.postTitle.text = post.post_title
        binding.postMessage.text = post.post_message

        binding.detailBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.editBtn.setOnClickListener {
            findNavController().navigate(DetailPostFragmentDirections.actionDetailPostFragmentToAddPostFragment(true))
            sharedViewModel.setSharedPost(post)
        }

    }

}