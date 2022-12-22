package com.fanonymous.jogjaistimewa.data.destinasi

import com.google.gson.annotations.SerializedName

data class DestinasiItem(

	@field:SerializedName("destinasi_kategori")
	val destinasiKategori: String,

	@field:SerializedName("id_destinasi")
	val idDestinasi: String,

	@field:SerializedName("destinasi_maps")
	val destinasiMaps: String,

	@field:SerializedName("destinasi_nama")
	val destinasiNama: String,

	@field:SerializedName("destinasi_phone")
	val destinasiPhone: String,

	@field:SerializedName("destinasi_image")
	val destinasiImage: String,

	@field:SerializedName("destinasi_web")
	val destinasiWeb: String,

	@field:SerializedName("destinasi_desc")
	val destinasiDesc: String
)