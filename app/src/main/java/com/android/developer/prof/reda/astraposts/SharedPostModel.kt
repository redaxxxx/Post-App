package com.android.developer.prof.reda.astraposts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.astraposts.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel(){

    private val _sharedPost = MutableStateFlow<Resource<PostModel>>(Resource.Unspecified())
    val sharedPost: StateFlow<Resource<PostModel>>
        get() = _sharedPost

    fun setSharedPost(postModel: PostModel){
        viewModelScope.launch {
            _sharedPost.emit(Resource.Success(postModel))
        }
    }
}