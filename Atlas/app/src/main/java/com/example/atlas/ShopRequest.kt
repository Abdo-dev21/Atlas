package com.example.atlas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShopRequest(val id:String, val uidp:String, val address:String, val shopps:String, val Shopper:String): Parcelable{
    constructor() : this("", "", "","","" )
}