package com.example.recipefinder.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import android.os.Parcel
import android.os.Parcelable

data class Recipe(
    @DocumentId
    val id: String = "",  // Firebase document ID

    @PropertyName("name")
    var name: String = "",

    @PropertyName("ingredients")
    var ingredients: List<String> = listOf(),  // Changed from Any to String

    @PropertyName("steps")
    var steps: List<String> = listOf(),  // Changed from Any to String

    @PropertyName("imageUri")
    var imageUri: String? = null,

    @PropertyName("userId")
    var userId: String = "",

    @PropertyName("vegan")
    var vegan: Boolean = false,

    @PropertyName("vegetarian")
    var vegetarian: Boolean = false
) : Parcelable {

    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeStringList(ingredients)
        parcel.writeStringList(steps)
        parcel.writeString(imageUri)
        parcel.writeString(userId)
        parcel.writeByte(if (vegan) 1 else 0)
        parcel.writeByte(if (vegetarian) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}