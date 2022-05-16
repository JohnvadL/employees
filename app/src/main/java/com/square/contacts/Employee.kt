package com.square.contacts

import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("uuid")
    val id: String?,

    @SerializedName("full_name")
    val name: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    @SerializedName("email_address")
    val email: String?,

    @SerializedName("biography")
    val biography: String?,

    @SerializedName("photo_url_small")
    val photoSmall: String?,

    @SerializedName("photo_url_large")
    val photoLarge: String?,

    @SerializedName("team")
    val team: String?,

    @SerializedName("employee_type")
    val employeeType: String?
)