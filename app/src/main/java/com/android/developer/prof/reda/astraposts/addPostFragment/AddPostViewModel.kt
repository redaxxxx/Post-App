package com.android.developer.prof.reda.astraposts.addPostFragment

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.astraposts.network.ApiService
import com.android.developer.prof.reda.astraposts.util.Resource
import com.android.developer.prof.reda.astraposts.util.createPartFromInt
import com.android.developer.prof.reda.astraposts.util.createPartFromString
import com.android.developer.prof.reda.astraposts.util.getImageMultipart
import com.android.developer.prof.reda.astraposts.util.getUpdateImageMultipart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
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

    fun updatePost(id: Int, imageUri: Uri, imageUrl: String, title: String, message: String,
                   contentResolver: ContentResolver, context: Context){
        val idPart = createPartFromInt(id)
        val titlePart = createPartFromString(title)
        val messagePart = createPartFromString(message)

        runBlocking {
            _addPost.emit(Resource.Loading())
        }

        viewModelScope.launch {
            try {

                if (imageUri != null) {
                    val imagePart = getImageMultipart(imageUri, contentResolver)
                    val response = apiService.updatePostWithFile(idPart, titlePart, messagePart, imagePart)

                    if (response.isSuccessful) {
                        _updatePost.emit(Resource.Success(response.body()!!))
                    } else {
                        _updatePost.emit(Resource.Failed(response.message()))
                    }
                }else if(imageUrl != null){
                    val imageUrlPart = getUpdateImageMultipart(imageUrl.toUri(), contentResolver, context)
                    val response = apiService.updatePostWithFile(idPart, titlePart, messagePart, imageUrlPart!!)
                    if (response.isSuccessful) {
                        _updatePost.emit(Resource.Success(response.body()!!))
                    } else {
                        _updatePost.emit(Resource.Failed(response.message()))
                    }

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