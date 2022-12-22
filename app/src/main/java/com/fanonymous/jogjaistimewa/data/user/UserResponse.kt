package com.fanonymous.jogjaistimewa.data.user

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("user")
	val user: List<UserItem>
)