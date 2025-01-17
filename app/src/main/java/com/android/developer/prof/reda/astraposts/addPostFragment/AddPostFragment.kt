package com.android.developer.prof.reda.astraposts.addPostFragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.astraposts.PostModel
import com.android.developer.prof.reda.astraposts.R
import com.android.developer.prof.reda.astraposts.SharedViewModel
import com.android.developer.prof.reda.astraposts.databinding.FragmentAddPostBinding
import com.android.developer.prof.reda.astraposts.util.Resource
import com.android.developer.prof.reda.astraposts.util.createPartFromString
import com.android.developer.prof.reda.astraposts.util.getImageMultipart
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AddPostFragment"

@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private val viewModel by viewModels<AddPostViewModel>()
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.main_nav)

    private var postArgs: PostModel? = null

    private var picUri: Uri? = null
    private var isUpdate: Boolean = false

    private val getImage: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            Glide.with(requireActivity())
                .load(it.toString())
                .into(binding.addPostPic)

            picUri = it

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_post,
            container,
            false
        )

        isUpdate = AddPostFragmentArgs.fromBundle(requireArguments()).isUpdate

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        if (isUpdate) {

            lifecycleScope.launch {
                sharedViewModel.sharedPost.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            postArgs = it.data
                            picUri = postArgs?.post_image?.toUri()
                            Log.d(TAG, "data is ${it.data}")
                        }

                        else -> Unit
                    }
                }
            }

            binding.publishBtn.text = getString(R.string.update_string)
            binding.publishBtn.text = getString(R.string.update_string)
            binding.addPostLabel.text = getString(R.string.update_post)
            binding.titleEt.setText(postArgs?.post_title.toString())
            binding.messageEt.setText(postArgs?.post_message.toString())

            Glide.with(requireActivity())
                .load(postArgs?.post_image)
                .into(binding.addPostPic)
        }

        binding.addPicBtn.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.publishBtn.setOnClickListener {
            if (isUpdate) {
                viewModel.updatePost(
                    postArgs!!.id,
                    picUri!!,
                    postArgs!!.post_image,
                    postArgs!!.post_title,
                    postArgs!!.post_message,
                    requireActivity().contentResolver,
                    requireActivity()

                )

            } else {
                viewModel.addNewPost(
                    getImageMultipart(picUri!!, requireActivity().contentResolver),
                    createPartFromString(binding.titleEt.text.toString()),
                    createPartFromString(binding.messageEt.text.toString())
                )
            }
        }

        //update post
        lifecycleScope.launch {
            viewModel.updatePost.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarAddPost.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarAddPost.visibility = View.GONE

                        findNavController().navigateUp()

                        binding.addPostPic.setImageResource(0)
                        binding.titleEt.text?.clear()
                        binding.messageEt.text?.clear()

                    }

                    is Resource.Failed -> {
                        binding.progressBarAddPost.visibility = View.GONE
                        Log.d(TAG, "update Error is ${it.message}")
                        Toast.makeText(
                            requireActivity(),
                            "Update Failed: ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        //add new post
        lifecycleScope.launch {
            viewModel.addPost.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarAddPost.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarAddPost.visibility = View.GONE

                        findNavController().navigateUp()

                        binding.addPostPic.setImageResource(0)
                        binding.titleEt.text?.clear()
                        binding.messageEt.text?.clear()

                    }

                    is Resource.Failed -> {
                        binding.progressBarAddPost.visibility = View.GONE
                        Log.d(TAG, "Add new post Error is ${it.message}")
                        Toast.makeText(
                            requireActivity(),
                            "Add Post Failed: ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}