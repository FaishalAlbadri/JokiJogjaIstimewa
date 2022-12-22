package com.fanonymous.jogjaistimewa.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.api.APIClient
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.BaseResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    @BindView(R.id.btn_login)
    lateinit var btnLogin: TextView

    @BindView(R.id.btn_register)
    lateinit var btnRegister: TextView

    @BindView(R.id.edt_nama)
    lateinit var edtNama: EditText

    @BindView(R.id.edt_email)
    lateinit var edtEmail: EditText

    @BindView(R.id.edt_password)
    lateinit var edtPassword: EditText

    @BindView(R.id.btn_password)
    lateinit var btnPassword: ImageView

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog

    private lateinit var apiInterface: APIInterface
    private lateinit var baseResponseCall: Call<BaseResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ButterKnife.bind(this)

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Register")

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)

    }

    @OnClick(R.id.btn_register)
    fun onBtnRegisterClicked() {
        val nama: String = edtNama.getText().toString()
        val email: String = edtEmail.getText().toString()
        val password: String = edtPassword.getText().toString()

        if (nama.trim().length > 0 && email.trim().length > 0 && password.trim().length > 0) {
            pd.show()
            register(nama, email, password)
        } else {
            alert.showAlertDialog(
                this@RegisterActivity,
                "Register gagal..",
                "Form tidak boleh kosong!",
                false
            )
        }
    }

    private fun register(nama: String, email: String, password: String) {
        baseResponseCall = apiInterface.register(nama, email, password)
        baseResponseCall.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        Toast.makeText(
                            this@RegisterActivity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    } else {
                        alert.showAlertDialog(
                            this@RegisterActivity,
                            "Register gagal..",
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
                    this@RegisterActivity,
                    "Register gagal..",
                    Server.CHECK_INTERNET_CONNECTION,
                    false
                )
            }

        })
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
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}