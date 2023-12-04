package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class LanguageEntity(
    val name: String?,
    val code: String?,
    val avatar: Int?,
    var isSelected: Boolean = false,
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readBoolean(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(code)
        if (avatar != null) {
            parcel.writeInt(avatar)
        }
        parcel.writeBoolean(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LanguageEntity> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): LanguageEntity {
            return LanguageEntity(parcel)
        }

        override fun newArray(size: Int): Array<LanguageEntity?> {
            return arrayOfNulls(size)
        }
    }
}