package com.android.myapplication.ui.disc.data_class

import android.os.Parcel
import android.os.Parcelable

data class DiscScore(
    var DScore: Int = 0,
    var IScore: Int = 0,
    var SScore: Int = 0,
    var CScore: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(DScore)
        parcel.writeInt(IScore)
        parcel.writeInt(SScore)
        parcel.writeInt(CScore)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiscScore> {
        override fun createFromParcel(parcel: Parcel): DiscScore {
            return DiscScore(parcel)
        }

        override fun newArray(size: Int): Array<DiscScore?> {
            return arrayOfNulls(size)
        }
    }
}