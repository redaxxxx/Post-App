package com.android.developer.prof.reda.astraposts.detailFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.astraposts.PostModel
import com.android.developer.prof.reda.astraposts.R
import com.android.developer.prof.reda.astraposts.SharedViewModel
import com.android.developer.prof.reda.astraposts.databinding.FragmentDetailPostBinding
import com.android.developer.prof.reda.astraposts.util.Resource
import com.android.developer.prof.reda.astraposts.util.createPartFromInt
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "DetailPostFragment"
@AndroidEntryPoint
class DetailPostFragment : Fragment() {

    private lateinit var binding: FragmentDetailPostBinding
    private lateinit var post: PostModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.main_nav)
    private val viewModel by viewModels<DetailPostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = DetailPostFragmentArgs.fromBundle(requireArguments()).post
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        binding.deleteBtn.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                setTitle("Delete item from cart")
                setMessage("Do you want to delete item from your cart?")
                setNegativeButton("Cancel"){dialog,_->
                    dialog.dismiss()
                }
                setPositiveButton("Delete"){dialog,_->
                    viewModel.deletePost(createPartFromInt(post.id))
                    dialog.dismiss()
                }
            }
            alertDialog.create()
            alertDialog.show()
        }

        lifecycleScope.launch {
            viewModel.deletePost.collectLatest {
                when(it){
                    is Resource.Success->{
                        findNavController().navigateUp()
                        Toast.makeText(requireActivity(), "Delete Post Success", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Failed->{
                        Log.d(TAG, "Delete Post Error: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }



}