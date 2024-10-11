package com.android.developer.prof.reda.astraposts.detailFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.astraposts.network.ApiService
import com.android.developer.prof.reda.astraposts.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject
@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel(){

    private val _deletePost = MutableStateFlow<Resource<ResponseBody>>(Resource.Unspecified())
    val deletePost: StateFlow<Resource<ResponseBody>>
        get() = _deletePost

    fun deletePost(id: RequestBody){
        runBlocking {
            _deletePost.emit(Resource.Loading())
        }

        viewModelScope.launch {
            try{
                val response = apiService.deletePost(id)
                if (response.isSuccessful){
                    _deletePost.emit(Resource.Success(response.body()!!))
                }else{
                    _deletePost.emit(Resource.Failed(response.message()))
                }

            }catch (e: Exception){
                _deletePost.emit(Resource.Failed(e.message.toString()))
            }
        }
    }
}