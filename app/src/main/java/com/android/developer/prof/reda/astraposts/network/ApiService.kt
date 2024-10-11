package com.android.developer.prof.reda.astraposts.network

import com.android.developer.prof.reda.astraposts.PostModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("getposts")
    suspend fun getPosts(): List<PostModel>

    @Multipart
    @POST("create")
    suspend fun createPost(
        @Part("post_title") postTitle: RequestBody,
        @Part("post_message") postMessage: RequestBody,
        @Part post_image: MultipartBody.Part
    ): Response<ResponseBody>

    @Multipart
    @POST("updatepost")
    suspend fun updatePost(
        @Part("id") id: RequestBody,
        @Part("post_title") postTitle: RequestBody,
        @Part("post_message") postMessage: RequestBody,
        @Part post_image: MultipartBody.Part
    ): Response<ResponseBody>

    @Multipart
    @POST("deletepost")
    suspend fun deletePost(
        @Part("id") id: RequestBody
    ): Response<ResponseBody>
}