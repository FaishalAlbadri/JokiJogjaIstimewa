package com.fanonymous.jogjaistimewa.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.adapter.HomeMenuAdapter
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.DataMenu
import com.fanonymous.jogjaistimewa.data.user.UserItem
import com.fanonymous.jogjaistimewa.data.user.UserResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import com.fanonymous.jogjaistimewa.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    @BindView(R.id.txt_username)
    lateinit var txtUsername: TextView

    @BindView(R.id.img_user)
    lateinit var imgUser: ImageView

    @BindView(R.id.rv_menu)
    lateinit var rvMenu: RecyclerView

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    private lateinit var homeMenuAdapter: HomeMenuAdapter
    private var itemMenuList: MutableList<DataMenu> = ArrayList()

    private val imgMenu = arrayOf(
        "menu_wisata",
        "menu_paket_wisata",
        "menu_kuliner",
        "menu_penginapan",
        "menu_desa_wisata",
        "menu_voucher",
        "menu_berita",
        "menu_info_layanan"
    )
    private val namaMenu = arrayOf(
        "Wisata",
        "Paket Wisata",
        "Kuliner",
        "Penginapan",
        "Desa Wisata",
        "Voucher",
        "Berita",
        "Info Layanan"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)

        pd = ProgressDialog(requireActivity())
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Loading")
        pd.show()

        sessionManager = SessionManager(requireActivity())

        apiInterface = APIClient.getRetrofit(requireActivity())!!.create(APIInterface::class.java)

        loadProfile(sessionManager.getIdUser()!!)
        loadMenu()

        return view
    }

    private fun loadMenu() {
        homeMenuAdapter = HomeMenuAdapter(itemMenuList)
        rvMenu.layoutManager = GridLayoutManager(requireActivity(), 4)
        rvMenu.adapter = homeMenuAdapter

        for (i in 0..namaMenu.size - 1) {
            itemMenuList.add(DataMenu(imgMenu[i], namaMenu[i]))
        }

        homeMenuAdapter.notifyDataSetChanged()
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
                        Glide.with(requireActivity())
                            .load(Server.BASE_URL_IMG + userItem.get(0).userImage)
                            .circleCrop()
                            .into(imgUser)
                        txtUsername.text = userItem.get(0).userName
                    } else {
                        alert.showAlertDialog(
                            requireActivity(),
                            "Profile gagal..",
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
                    requireActivity(),
                    "Profile gagal..",
                    Server.CHECK_INTERNET_CONNECTION,
                    false
                )
            }

        })
    }
}