package com.teamox.woongstock.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamox.woongstock.R
import com.teamox.woongstock.model.HistoryList

class HistoryRecyclerview(private val mContext: Context, private val items: List<HistoryList>): RecyclerView.Adapter<HistoryRecyclerview.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName = itemView.findViewById<TextView>(R.id.tv_product_name)
        val ivProduct = itemView.findViewById<ImageView>(R.id.iv_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = items[position].productQuantity
        Glide.with(mContext)
            .load(items[position].productImage)
            .error(R.drawable.box)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(holder.ivProduct)

    }

    override fun getItemCount(): Int {
        return items.size
    }


}
