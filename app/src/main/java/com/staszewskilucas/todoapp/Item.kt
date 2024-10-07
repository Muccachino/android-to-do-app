package com.staszewskilucas.todoapp

import android.os.Parcel
import android.os.Parcelable

data class Item (
    val name: String,
    var isDone: Boolean = false
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeByte(if(isDone) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR: Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item = Item(parcel)
        override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
    }
}