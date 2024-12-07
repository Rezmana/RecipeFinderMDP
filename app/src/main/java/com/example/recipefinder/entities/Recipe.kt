package com.example.recipefinder.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize


@Entity(tableName = "recipes")
@Parcelize
data class Recipe(
    @PrimaryKey val id: String = "", // Use Firestore document ID as the primary key
    var recipeName: String = "",
    var userName: String = "",
    var recipeDescription: String = "",
    var prepSteps: List<String> = listOf(), // Requires TypeConverter
    var cookSteps: List<String> = listOf(), // Requires TypeConverter
    var ingredients: List<String> = listOf(), // Requires TypeConverter
//    var steps: List<String> = listOf(), // Requires TypeConverter
    var imageUri: String? = null,
    var typeCuisine: String? = null,
    var difficulty: String? = null,
    var prepTime: String? = null,
    var cookingTime: String? = null,
    var userId: String = "",
    var vegan: Boolean = false,
    var vegetarian: Boolean = false
) : Parcelable {

    // Parcelable implementation for passing Recipe objects between fragments or activities
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
//        parcel.createStringArrayList() ?: listOf(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int = 0

    companion object : Parceler<Recipe> {

        override fun Recipe.write(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(recipeName)
            parcel.writeString(userName)
            parcel.writeStringList(ingredients)
            parcel.writeString(recipeDescription)
//            parcel.writeStringList(steps)
            parcel.writeString(imageUri)
            parcel.writeString(typeCuisine)
            parcel.writeString(difficulty)
            parcel.writeString(prepTime)
            parcel.writeString(cookingTime)
            parcel.writeString(userId)
            parcel.writeByte(if (vegan) 1 else 0)
            parcel.writeByte(if (vegetarian) 1 else 0)
        }

        override fun create(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }
    }
}
