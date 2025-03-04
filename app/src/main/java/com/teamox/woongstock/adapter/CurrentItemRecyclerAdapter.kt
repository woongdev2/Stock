package com.teamox.woongstock.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamox.woongstock.R
import com.teamox.woongstock.activity.ChangeActivity
import com.teamox.woongstock.activity.ProductDetailActivity
import com.teamox.woongstock.data.CurrentItem
import com.teamox.woongstock.data.StockTable

class CurrentItemRecyclerAdapter(var itemList: List<StockTable>, val context:Context)
    : RecyclerView.Adapter<CurrentItemRecyclerAdapter.CurrentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.current_item_recycler_view, parent, false)
        return CurrentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        Glide.with(context)
            .load(itemList[position].image)
            .error(R.drawable.box)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(holder.iv_image)
        holder.tv_name.text = itemList[position].name
        holder.tv_price.text = "${itemList[position].price}원"
        holder.tv_quantity.text = "${itemList[position].quantity}개 남았어요"
        holder.btnModify.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("db_key",itemList[position].id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun setItem(filteredList: List<StockTable>) {
        itemList = filteredList
        notifyDataSetChanged()
    }

    inner class CurrentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_image = itemView.findViewById<ImageView>(R.id.iv_product)
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_price = itemView.findViewById<TextView>(R.id.tv_price)
        val tv_quantity = itemView.findViewById<TextView>(R.id.tv_quantity)
        val btnModify = itemView.findViewById<ImageButton>(R.id.btn_modify)
    }
}