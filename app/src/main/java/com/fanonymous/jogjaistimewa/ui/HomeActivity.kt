package com.fanonymous.jogjaistimewa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.ui.fragment.FavoriteFragment
import com.fanonymous.jogjaistimewa.ui.fragment.HomeFragment
import com.fanonymous.jogjaistimewa.ui.fragment.ProfileFragment
import com.fanonymous.jogjaistimewa.ui.fragment.SpinWheelFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    @BindView(R.id.bottomNav)
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        loadFragment(HomeFragment())
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.favorite -> {
                    loadFragment(FavoriteFragment())
                    true
                }
                R.id.games -> {
                    loadFragment(SpinWheelFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}