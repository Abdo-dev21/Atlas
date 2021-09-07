package com.example.atlas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid:String, val username:String, val profileImageUrl: String, val firstname: String,
val lastname:String, val idnumb: String, val ntel: String,
           val address:String, val points:Int,  val completeflag:String, val Notificationflag:String ): Parcelable{
    constructor() : this("", "", "", "","","",
        "","",0, "", "" )
}