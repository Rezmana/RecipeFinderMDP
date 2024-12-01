package com.example.recipefinder.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

class users {
    @DocumentId
    private val username: String? = null

    @PropertyName("email")
    private val email: String? = null

    @DocumentId()
    private val recipes: List<String>? = null

    @PropertyName("ProfilePicture")
    private val profilePicture: String? = null

    @DocumentId
    private val savedRecipes: List<String>? = null
}