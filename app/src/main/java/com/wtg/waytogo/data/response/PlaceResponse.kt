package com.wtg.waytogo.data.response

import com.google.gson.annotations.SerializedName

data class PlaceResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("PlaceResponse")
	val placeResponse: List<PlaceResponseItem>? = emptyList()
)

data class PlaceResponseItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("photo_reference")
	val photoReference: String? = null,

	@field:SerializedName("lng")
	val lng: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null,

	@field:SerializedName("formatted_phone_number")
	val formattedPhoneNumber: String? = null,

	@field:SerializedName("lat")
	val lat: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)
