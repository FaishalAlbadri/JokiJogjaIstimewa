package com.fanonymous.jogjaistimewa.ui.fragment

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.user.UserItem
import com.fanonymous.jogjaistimewa.data.user.UserResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import com.fanonymous.jogjaistimewa.util.SessionManager
import com.fanonymous.jogjaistimewa.util.luckydraw.LuckyWheel
import com.fanonymous.jogjaistimewa.util.luckydraw.OnLuckyWheelReachTheTarget
import com.fanonymous.jogjaistimewa.util.luckydraw.WheelItem
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SpinWheelFragment : Fragment() {

    @BindView(R.id.btn_spin)
    lateinit var btnSpin: MaterialButton

    @BindView(R.id.sw_kupon)
    lateinit var swKupon: LuckyWheel

    @BindView(R.id.txt_count)
    lateinit var txtGachaPoint: TextView

    private var item: MutableList<WheelItem> = ArrayList()
    private var points: Int = 0
    private var gachaPoints = 0

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    private val nama = arrayOf(
        "Zonk", "20%", "30%", "Zonk", "Zonk", "10%"
    )
    private val warna = arrayOf(
        "#75caee", "#b072d3", "#49935a", "#da615c", "#ecc962", "#4381e5"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spin_wheel, container, false)
        ButterKnife.bind(this, view)
        setView()
        setDataGacha()

        txtGachaPoint.text = "X " + gachaPoints.toString()

        pd = ProgressDialog(requireActivity())
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Loading")
        pd.show()

        sessionManager = SessionManager(requireActivity())

        apiInterface = APIClient.getRetrofit(requireActivity())!!.create(APIInterface::class.java)

        loadProfile(sessionManager.getIdUser()!!)

        return view
    }

    private fun loadProfile(idUser: String) {
        userResponseCall = apiInterface.profile(idUser)
        userResponseCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val userResponse: UserResponse = response.body()!!
                        val userItem: List<UserItem> = userResponse.user
                        gachaPoints = userItem.get(0).userPoint.toInt()
                        txtGachaPoint.text = "X " + gachaPoints.toString()
                    } else {
                        alert.showAlertDialog(
                            requireActivity().applicationContext,
                            "SpinWheel gagal..",
                            response.body()!!.msg,
                            false
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(
                    requireActivity().applicationContext,
                    "Profile gagal..",
                    Server.CHECK_INTERNET_CONNECTION,
                    false
                )
            }

        })
    }

    private fun setView() {
        swKupon.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
            override fun onReachTarget() {
                val data: WheelItem = item.get(points - 1)
                val nama = data.text
                if (nama.equals("Zonk")) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Yahh.. Kamu belum mendapatkan diskon",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Kupon Kamu")
                        .setMessage("Yeeyy.. Kamu mendapatkan kupon diskon sebesar " + nama)
                        .setPositiveButton("Claim") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
                randomData()
            }
        })
    }

    private fun setDataGacha() {
        for (i in 0..nama.size - 1) {
            item.add(
                WheelItem(
                    Color.parseColor(warna[i]),
                    nama[i]
                )
            )
        }

        swKupon.addWheelItems(item, MediaPlayer.create(requireActivity(), R.raw.luckyspin))

        randomData()
    }

    private fun randomData() {
        val random = Random()
        points = random.nextInt(item.size + 1)
        if (points == 0 || points == item.size + 1) {
            points = 1
        }
    }

    @OnClick(R.id.btn_spin)
    fun onBtnSpinClicked() {
        if (gachaPoints > 0) {
            gachaPoints = gachaPoints - 1
            txtGachaPoint.text = "X " + gachaPoints.toString()
            swKupon.rotateWheelTo(points)
        } else {
            Toast.makeText(
                requireActivity().applicationContext,
                "Yaahhh... Point gacha-mu sudah habis!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}