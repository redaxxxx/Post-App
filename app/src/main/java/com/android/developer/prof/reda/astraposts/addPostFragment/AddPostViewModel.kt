package com.android.developer.prof.reda.astraposts.addPostFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.astraposts.network.ApiService
import com.android.developer.prof.reda.astraposts.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel(){

    private val _addPost = MutableStateFlow<Resource<ResponseBody>>(Resource.Unspecified())
    val addPost: StateFlow<Resource<ResponseBody>>
        get() = _addPost

    private val _updatePost = MutableStateFlow<Resource<ResponseBody>>(Resource.Unspecified())
    val updatePost: StateFlow<Resource<ResponseBody>>
        get() = _updatePost

    fun updatePost(id: RequestBody, imagePart: MultipartBody.Part, title: RequestBody, message: RequestBody){
        runBlocking {
            _addPost.emit(Resource.Loading())
        }

        viewModelScope.launch {
            try {
                val response = apiService.updatePost(id, title, message, imagePart)

                if (response.isSuccessful){
                    _updatePost.emit(Resource.Success(response.body()!!))
                }else{
                    _updatePost.emit(Resource.Failed(response.message()))
                }
            }catch (e: Exception){
                _updatePost.emit(Resource.Failed(e.message.toString()))
            }
        }
    }

    fun addNewPost(imagePart: MultipartBody.Part, title: RequestBody, message: RequestBody){
        runBlocking {
            _addPost.emit(Resource.Loading())
        }

        viewModelScope.launch {
            try {
                val response = apiService.createPost(title, message, imagePart)
                if (response.isSuccessful){
                    _addPost.emit(Resource.Success(response.body()!!))
                }else{
                    _addPost.emit(Resource.Failed(response.message()))
                }

            }catch (e: Exception){
                _addPost.emit(Resource.Failed(e.message.toString()))
            }
        }
    }
}