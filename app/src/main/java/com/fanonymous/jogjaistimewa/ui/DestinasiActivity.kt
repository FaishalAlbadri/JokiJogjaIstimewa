package com.fanonymous.jogjaistimewa.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.adapter.DestinasiAdapter
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.destinasi.DestinasiItem
import com.fanonymous.jogjaistimewa.data.destinasi.DestinasiResponse
import com.fanonymous.jogjaistimewa.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinasiActivity : AppCompatActivity() {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.txt_title)
    lateinit var txtTitle: TextView

    @BindView(R.id.rv_destinasi)
    lateinit var rvDestinasi: RecyclerView

    private lateinit var kategori: String

    private lateinit var pd: ProgressDialog

    private lateinit var apiInterface: APIInterface
    private lateinit var destinasiResponseCall: Call<DestinasiResponse>
    private lateinit var destinasiAdapter: DestinasiAdapter
    private var item: ArrayList<DestinasiItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinasi)
        ButterKnife.bind(this)

        kategori = intent.getStringExtra("kategori").toString()
        txtTitle.text = "Pilihan Destinasi " + kategori

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Loading")
        pd.show()

        destinasiAdapter = DestinasiAdapter(item)
        rvDestinasi.layoutManager = LinearLayoutManager(this)
        rvDestinasi.adapter = destinasiAdapter


        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)
        loadDestinasi()
    }

    private fun loadDestinasi() {
        destinasiResponseCall = apiInterface.destinasi(kategori)
        destinasiResponseCall.enqueue(object : Callback<DestinasiResponse> {
            override fun onResponse(
                call: Call<DestinasiResponse>,
                response: Response<DestinasiResponse>
            ) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val destinasiResponse = response.body()!!
                        destinasiAdapter.delete()
                        item.clear()
                        item.addAll(destinasiResponse.destinasi)
                        destinasiAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@DestinasiActivity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                Toast.makeText(
                    this@DestinasiActivity,
                    Server.CHECK_INTERNET_CONNECTION,
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }

        })
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }
}