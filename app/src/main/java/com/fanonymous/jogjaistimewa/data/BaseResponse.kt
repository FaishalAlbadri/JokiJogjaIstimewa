package com.fanonymous.jogjaistimewa.data

import com.google.gson.annotations.SerializedName

data class BaseResponse(

	@field:SerializedName("msg")
	val msg: String
)