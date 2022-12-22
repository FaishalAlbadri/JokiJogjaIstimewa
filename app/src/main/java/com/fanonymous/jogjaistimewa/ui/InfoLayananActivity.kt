package com.fanonymous.jogjaistimewa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.jogjaistimewa.R

class InfoLayananActivity : AppCompatActivity() {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_layanan)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }
}