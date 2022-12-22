package com.fanonymous.jogjaistimewa.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.btn_login)
    lateinit var btnLogin: TextView

    @BindView(R.id.btn_register)
    lateinit var btnRegister: TextView

    @BindView(R.id.edt_email)
    lateinit var edtEmail: EditText

    @BindView(R.id.edt_password)
    lateinit var edtPassword: EditText

    @BindView(R.id.btn_password)
    lateinit var btnPassword: ImageView

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Login")

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)

    }

    @OnClick(R.id.btn_password)
    fun onBtnPasswordClicked() {
        if (edtPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
            edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    @OnClick(R.id.btn_login)
    fun onBtnLoginClicked() {

        val email: String = edtEmail.getText().toString()
        val password: String = edtPassword.getText().toString()

        if (email.trim().length > 0 && password.trim().length > 0) {
            pd.show()
            login(email, password)
        } else {
            alert.showAlertDialog(
                this@LoginActivity,
                "Login gagal..",
                "Form tidak boleh kosong!",
                false
            )
        }

    }

    private fun login(email: String, password: String) {
        userResponseCall = apiInterface.login(email, password)
        userResponseCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val userResponse: UserResponse = response.body()!!
                        val userItem: List<UserItem> = userResponse.user
                        val idUser: String = userItem.get(0).idUser
                        sessionManager.createUser(idUser)
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                    } else {
                        alert.showAlertDialog(
                            this@LoginActivity,
                            "Login gagal..",
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
                    this@LoginActivity,
                    "Login gagal..",
                    Server.CHECK_INTERNET_CONNECTION,
                    false
                )
            }

        })
    }


    @OnClick(R.id.btn_register)
    fun onBtnRegisterClicked() {
        startActivity(Intent(applicationContext, RegisterActivity::class.java))
    }

}