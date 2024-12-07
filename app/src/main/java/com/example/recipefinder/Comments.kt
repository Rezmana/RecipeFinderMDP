package com.example.recipefinder
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comments(
    val userName: String = "",
    val commentText: String = "",
    val timeStamp: Long = 0L
)