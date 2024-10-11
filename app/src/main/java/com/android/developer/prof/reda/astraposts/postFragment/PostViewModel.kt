package com.android.developer.prof.reda.astraposts.postFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.astraposts.PostModel
import com.android.developer.prof.reda.astraposts.network.ApiService
import com.android.developer.prof.reda.astraposts.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {

    private val _posts = MutableStateFlow<Resource<List<PostModel>>>(Resource.Unspecified())
    val posts: StateFlow<Resource<List<PostModel>>>
        get() = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts(){
        runBlocking {
            _posts.emit(Resource.Loading())
        }
        viewModelScope.launch {
            try{
                val posts = apiService.getPosts()

                _posts.emit(Resource.Success(posts))

            }catch (e: Exception){
                _posts.emit(Resource.Failed(e.message.toString()))
            }
        }
    }
}