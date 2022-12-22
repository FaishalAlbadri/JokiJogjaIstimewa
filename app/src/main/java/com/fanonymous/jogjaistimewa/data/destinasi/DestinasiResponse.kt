package com.fanonymous.jogjaistimewa.data.destinasi

import com.google.gson.annotations.SerializedName

data class DestinasiResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("destinasi")
	val destinasi: List<DestinasiItem>
)