package com.fanonymous.jogjaistimewa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.data.DataMenu
import org.jetbrains.annotations.NotNull

class WishlistAdapter : RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private var list: MutableList<DataMenu>
    private lateinit var context: Context

    constructor(list: MutableList<DataMenu>) : super() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list.get(position)
        Glide.with(holder.imgWishlist)
            .load(getImage(data.img))
            .centerInside()
            .into(holder.imgWishlist)

        holder.txtWishlist.text = data.nama
    }

    class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txt_wishlist)
        lateinit var txtWishlist: TextView

        @BindView(R.id.img_wishlist)
        lateinit var imgWishlist: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun delete() {
        val size: Int = list.size
        if (size > 0) {
            for (i in 0 until size) {
                list.removeAt(0)
            }
            notifyItemRangeChanged(0, size)
        }
    }

    fun getImage(imgName: String): Int {
        var drawableResourceId: Int =
            context.resources.getIdentifier(imgName, "drawable", context.packageName)
        return drawableResourceId
    }
}