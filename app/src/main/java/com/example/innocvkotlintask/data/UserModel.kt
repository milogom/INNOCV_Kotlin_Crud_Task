package com.example.innocvkotlintask.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String?,
    @SerializedName("birthdate") var birthdate: String?
    ) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString(),
        birthdate = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(birthdate.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

}