package com.wtg.waytogo.data.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class User(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean? = null,

	@field:SerializedName("isAnonymous")
	val isAnonymous: Boolean? = null,

	@field:SerializedName("displayName")
	val displayName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
