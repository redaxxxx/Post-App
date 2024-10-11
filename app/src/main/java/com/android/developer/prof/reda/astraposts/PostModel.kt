package com.android.developer.prof.reda.astraposts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    val id: Int,
    val post_title: String,
    val post_message: String,
    val post_image: String
): Parcelable{
    constructor(): this(0,"","","")
}