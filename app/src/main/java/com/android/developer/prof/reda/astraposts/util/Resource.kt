package com.android.developer.prof.reda.astraposts.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Loading<T>: Resource<T>()
    class Success<T>(data: T): Resource<T>(data)
    class Failed<T>(message: String): Resource<T>(message = message)
    class Unspecified<T>: Resource<T>()
}
