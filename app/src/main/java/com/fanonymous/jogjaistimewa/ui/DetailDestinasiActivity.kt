package com.fanonymous.jogjaistimewa.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.BaseResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import com.fanonymous.jogjaistimewa.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailDestinasiActivity : AppCompatActivity() {

    private lateinit var id: String
    private lateinit var nama: String
    private lateinit var desc: String
    private lateinit var img: String
    private lateinit var maps: String
    private lateinit var phone: String
    private lateinit var web: String
    private lateinit var kategori: String

    @BindView(R.id.txt_title)
    lateinit var txtTitle: TextView

    @BindView(R.id.txt_detail)
    lateinit var txtDetail: TextView

    @BindView(R.id.img_detail)
    lateinit var imgDetail: ImageView

    @BindView(R.id.rating)
    lateinit var ratingBar: RatingBar

    @BindView(R.id.edt_desc_ulasan)
    lateinit var edtDescUlasan: EditText

    @BindView(R.id.btn_ulasan)
    lateinit var btnUlasan: TextView

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog

    private lateinit var apiInterface: APIInterface
    private lateinit var baseResponseCall: Call<BaseResponse>
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_destinasi)
        ButterKnife.bind(this)
        getDataIntent()
        setView()
    }

    private fun setView() {
        txtTitle.text = nama
        txtDetail.text = desc
        Glide.with(this)
            .load(Server.BASE_URL_IMG + img)
            .centerCrop()
            .into(imgDetail)

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Menambahkan Wishlist")

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)
    }

    private fun getDataIntent() {
        id = intent.getStringExtra("id").toString()
        nama = intent.getStringExtra("nama").toString()
        desc = intent.getStringExtra("desc").toString()
        img = intent.getStringExtra("img").toString()
        maps = intent.getStringExtra("maps").toString()
        phone = intent.getStringExtra("phone").toString()
        web = intent.getStringExtra("web").toString()
        kategori = intent.getStringExtra("kat").toString()
    }

    @OnClick(R.id.btn_ulasan)
    fun onBtnUlasanClicked() {
        val rating = Math.round(ratingBar.rating)
        if(edtDescUlasan.text.toString().trim().length > 0 && rating > 0) {
            onBackPressed()
        } else {
            alert.showAlertDialog(
                this@DetailDestinasiActivity,
                "Ulasan Kosong!",
                "Kamu belum memberi ulasan",
                false
            )
        }
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }

    @OnClick(R.id.btn_web)
    fun onBtnWebClicked() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(web)))
    }

    @OnClick(R.id.btn_rute)
    fun onBtnRuteClicked() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(maps)))
    }

    @OnClick(R.id.btn_telephone)
    fun onBtnTelephoneClicked() {
        if (phone.equals("0") || phone.trim().length <= 0) {
            Toast.makeText(this, "Tidak Ada Nomor Telephone", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phone)))
        }
    }

    @OnClick(R.id.btn_wishlist)
    fun onBtnWishlistClicked() {
        pd.show()
        baseResponseCall = apiInterface.favorite(sessionManager.getIdUser()!!, id, kategori)
        baseResponseCall.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil") || response.body()!!.msg.equals("Sudah Tersimpan")) {
                        Toast.makeText(
                            this@DetailDestinasiActivity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        alert.showAlertDialog(
                            this@DetailDestinasiActivity,
                            "Favorite gagal..",
                            response.body()!!.msg,
                            false
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(
                    this@DetailDestinasiActivity,
                    "Favorite gagal..",
                    Server.CHECK_INTERNET_CONNECTION,
                    false
                )
            }

        })
    }
}