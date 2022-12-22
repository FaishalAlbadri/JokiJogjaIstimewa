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
import com.fanonymous.jogjaistimewa.api.Server
import com.fanonymous.jogjaistimewa.data.destinasi.DestinasiItem
import com.fanonymous.jogjaistimewa.ui.DetailDestinasiActivity
import org.jetbrains.annotations.NotNull

class DestinasiAdapter : RecyclerView.Adapter<DestinasiAdapter.ViewHolder> {

    private var list: MutableList<DestinasiItem>
    private lateinit var context: Context

    constructor(list: MutableList<DestinasiItem>) : super() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destinasi, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list.get(position)
        Glide.with(holder.imgDestinasi)
            .load(Server.BASE_URL_IMG + data.destinasiImage)
            .centerCrop()
            .into(holder.imgDestinasi)

        holder.txtDestinasi.text = data.destinasiNama

        holder.btnDestinasi.setOnClickListener {
            context.startActivity(
                Intent(context, DetailDestinasiActivity::class.java)
                    .putExtra("id", data.idDestinasi)
                    .putExtra("nama", data.destinasiNama)
                    .putExtra("desc", data.destinasiDesc)
                    .putExtra("img", data.destinasiImage)
                    .putExtra("maps", data.destinasiMaps)
                    .putExtra("phone", data.destinasiPhone)
                    .putExtra("web", data.destinasiWeb)
                    .putExtra("kat", data.destinasiKategori)
            )
        }
    }

    class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txt_destinasi)
        lateinit var txtDestinasi: TextView

        @BindView(R.id.img_destinasi)
        lateinit var imgDestinasi: ImageView

        @BindView(R.id.btn_destinasi)
        lateinit var btnDestinasi: ConstraintLayout

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
}