package com.fanonymous.jogjaistimewa.ui.fragment

import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.user.UserItem
import com.fanonymous.jogjaistimewa.data.user.UserResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import com.fanonymous.jogjaistimewa.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    @BindView(R.id.btn_logout)
    lateinit var btnLogout: TextView

    @BindView(R.id.txt_username)
    lateinit var txtUsername: TextView

    @BindView(R.id.txt_email)
    lateinit var txtEmail: TextView

    @BindView(R.id.img_user)
    lateinit var imgUser: ImageView

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        ButterKnife.bind(this, view)

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
                        Glide.with(requireActivity())
                            .load(Server.BASE_URL_IMG + userItem.get(0).userImage)
                            .circleCrop()
                            .into(imgUser)
                        txtUsername.text = userItem.get(0).userName
                        txtEmail.text = userItem.get(0).userEmail
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

    @OnClick(R.id.btn_logout)
    fun onBtnLogoutClicked() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Logout Akun")
            .setMessage("Yakinkah kamu ingin keluar dari akunmu?")
            .setPositiveButton("Logout") { dialogInterface, i ->
                sessionManager.logout()
            }
            .setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }
}