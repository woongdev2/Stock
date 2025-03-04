package com.teamox.woongstock.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.teamox.woongstock.model.ProductTable
import com.teamox.woongstock.view.ItemDetailActivity

class ProductListRecyclerview(private var items: List<ProductTable>, private val mContext: Context): RecyclerView.Adapter<ProductListRecyclerview.ViewHolder>()
{

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tv_product_name)
        val tvPrice = itemView.findViewById<TextView>(R.id.tv_product_price)
        val tvQuantity = itemView.findViewById<TextView>(R.id.tv_product_quantity)
        val ivProduct = itemView.findViewById<ImageView>(R.id.iv_product)

        fun bind(item: Int){
            itemView.apply {
                // 전체 레이아웃에 클릭 리스너 설정
                setOnClickListener {
                    val intent = Intent(mContext, ItemDetailActivity::class.java)
                    intent.putExtra("item_id", item)
                    mContext.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = items[position].name
        holder.tvPrice.text = items[position].sellingPrice + "원"
        holder.tvQuantity.text =  items[position].quantity
        Glide.with(mContext)
            .load(items[position].image)
            .error(R.drawable.box)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(holder.ivProduct)

        holder.bind(items[position].id)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun  setItem(filteredList: List<ProductTable>) {
        items = filteredList
        notifyDataSetChanged()
    }



}
