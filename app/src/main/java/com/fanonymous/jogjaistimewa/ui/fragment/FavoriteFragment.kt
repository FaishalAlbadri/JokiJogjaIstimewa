package com.fanonymous.jogjaistimewa.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.adapter.HomeMenuAdapter
import com.fanonymous.jogjaistimewa.adapter.WishlistAdapter
import com.fanonymous.jogjaistimewa.api.APIInterface
import com.fanonymous.jogjaistimewa.data.DataMenu
import com.fanonymous.jogjaistimewa.data.user.UserResponse
import com.fanonymous.jogjaistimewa.util.AlertDialogManager
import com.fanonymous.jogjaistimewa.util.SessionManager
import retrofit2.Call

class FavoriteFragment : Fragment() {

    @BindView(R.id.rv_wishlist)
    lateinit var rvWishlist: RecyclerView

    private lateinit var wishlistAdapter: WishlistAdapter
    private var itemMenuList: MutableList<DataMenu> = ArrayList()

    private val imgMenu = arrayOf(
        "menu_wisata",
        "menu_kuliner",
        "menu_penginapan",
        "menu_paket_wisata",
        "menu_desa_wisata"
    )
    private val namaMenu = arrayOf(
        "Wisata",
        "Kuliner",
        "Penginapan",
        "Paket Wisata",
        "Desa Wisata"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        ButterKnife.bind(this, view)
        loadMenu()
        return view
    }

    private fun loadMenu() {
        wishlistAdapter = WishlistAdapter(itemMenuList)
        rvWishlist.layoutManager = LinearLayoutManager(requireActivity())
        rvWishlist.adapter = wishlistAdapter

        for (i in 0..namaMenu.size - 1) {
            itemMenuList.add(DataMenu(imgMenu[i], namaMenu[i]))
        }

        wishlistAdapter.notifyDataSetChanged()
    }

}