package com.example.recipefinder.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Recipe_Copy(
    @DocumentId
    val id: String = "",  // Firebase document ID

    @PropertyName("name")
    var recipeName: String = "",

    @PropertyName("userName")
    var userName: String = "",  // User's name (creator or owner)

    @PropertyName("Description")
    var recipeDescription: String = "",  // User's name (creator or owner)

    @PropertyName("ingredients")
    var ingredients: List<String> = listOf(),  // Ingredients list

    @PropertyName("steps")
    var steps: List<String> = listOf(),  // Cooking steps

    @PropertyName("imageUri")
    var imageUri: String? = null,  // Recipe image URL

    @PropertyName("typeCuisine")
    var typeCuisine: String? = null,  // Cuisine type

    @PropertyName("difficulty")
    var difficulty: String? = null,  // Difficulty level

    @PropertyName("prepTime")
    var prepTime: String? = null,  // Preparation time

    @PropertyName("cookingTime")
    var cookingTime: String? = null,  // Cooking time

    @PropertyName("userId")
    var userId: String = "",  // Firebase user ID of the recipe creator or owner

    @PropertyName("vegan")
    var vegan: Boolean = false,  // Indicates if the recipe is vegan

    @PropertyName("vegetarian")
    var vegetarian: Boolean = false  // Indicates if the recipe is vegetarian
) : Parcelable {

    // Parcelable implementation for passing Recipe objects between fragments or activities
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(recipeName)
        parcel.writeString(userName)
        parcel.writeStringList(ingredients)
        parcel.writeString(recipeDescription)
        parcel.writeStringList(steps)
        parcel.writeString(imageUri)
        parcel.writeString(typeCuisine)
        parcel.writeString(difficulty)
        parcel.writeString(prepTime)
        parcel.writeString(cookingTime)
        parcel.writeString(userId)
        parcel.writeByte(if (vegan) 1 else 0)
        parcel.writeByte(if (vegetarian) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Recipe_Copy> {
        override fun createFromParcel(parcel: Parcel): Recipe_Copy {
            return Recipe_Copy(parcel)
        }

        override fun newArray(size: Int): Array<Recipe_Copy?> {
            return arrayOfNulls(size)
        }
    }
}
