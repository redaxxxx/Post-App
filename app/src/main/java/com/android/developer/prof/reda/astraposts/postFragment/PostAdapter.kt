package com.android.developer.prof.reda.astraposts.postFragment

import android.content.res.ColorStateList
import android.icu.number.NumberFormatter.UnitWidth
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.astraposts.PostModel
import com.android.developer.prof.reda.astraposts.R
import com.android.developer.prof.reda.astraposts.databinding.PostViewHolderBinding
import com.bumptech.glide.Glide

class PostAdapter: ListAdapter<PostModel, PostAdapter.PostViewHolder>(DiffCallback) {

    inner class PostViewHolder(private val binding: PostViewHolderBinding): ViewHolder(binding.root){
        fun bind(post: PostModel){
            Glide.with(itemView)
                .load(post.post_image)
                .into(binding.pic)

            binding.title.text = post.post_title

            itemView.setOnClickListener {
                onClickPost?.invoke(post)
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<PostModel>(){
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem.post_title == newItem.post_title
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    var onClickPost: ((PostModel) -> Unit)? = null
}