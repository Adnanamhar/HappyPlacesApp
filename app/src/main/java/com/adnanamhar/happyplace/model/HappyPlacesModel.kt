package com.adnanamhar.happyplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HappyPlacesModel (
    val id: Int,
    val title: String?,
    val image: String?,
    val description: String?,
    val date: String?,
    val location: String?,
    val latitude: Double,
    val longitude: Double
) : Parcelable
