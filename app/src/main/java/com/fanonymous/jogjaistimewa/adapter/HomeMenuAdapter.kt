package com.fanonymous.jogjaistimewa.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.fanonymous.jogjaistimewa.R
import com.fanonymous.jogjaistimewa.data.DataMenu
import com.fanonymous.jogjaistimewa.ui.DestinasiActivity
import com.fanonymous.jogjaistimewa.ui.InfoLayananActivity
import org.jetbrains.annotations.NotNull

class HomeMenuAdapter : RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {

    private var list: MutableList<DataMenu>
    private lateinit var context: Context

    constructor(list: MutableList<DataMenu>) : super() {
        this.list = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list.get(position)
        Glide.with(holder.imgMenu)
            .load(getImage(data.img))
            .centerInside()
            .into(holder.imgMenu)

        holder.txtMenu.text = data.nama

        holder.btnMenu.setOnClickListener {
            if (position == 0) {
                context.startActivity(
                    Intent(context, DestinasiActivity::class.java)
                        .putExtra("kategori", "Wisata")
                )
            } else if (position == 1) {

            } else if (position == 2) {
                context.startActivity(
                    Intent(context, DestinasiActivity::class.java)
                        .putExtra("kategori", "Kuliner")
                )
            } else if (position == 3) {
                context.startActivity(
                    Intent(context, DestinasiActivity::class.java)
                        .putExtra("kategori", "Penginapan")
                )
            } else if (position == 4) {
                context.startActivity(
                    Intent(context, DestinasiActivity::class.java)
                        .putExtra("kategori", "Desa Wisata")
                )
            } else if (position == 5) {

            } else if (position == 6) {

            }  else {
                context.startActivity(Intent(context, InfoLayananActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txt_menu)
        lateinit var txtMenu: TextView

        @BindView(R.id.img_menu)
        lateinit var imgMenu: ImageView

        @BindView(R.id.btn_menu)
        lateinit var btnMenu: ConstraintLayout

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    fun getImage(imgName: String): Int {
        var drawableResourceId: Int =
            context.resources.getIdentifier(imgName, "drawable", context.packageName)
        return drawableResourceId
    }
}