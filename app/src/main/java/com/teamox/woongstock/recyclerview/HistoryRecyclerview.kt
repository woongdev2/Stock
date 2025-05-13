package com.teamox.woongstock.recyclerview

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamox.woongstock.R
import com.teamox.woongstock.model.HistoryList
import org.w3c.dom.Text

class HistoryRecyclerview(private val mContext: Context, private val items: List<HistoryList>): RecyclerView.Adapter<HistoryRecyclerview.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvProductTitle = itemView.findViewById<TextView>(R.id.tv_product_title)
        val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
        val tvQuantity = itemView.findViewById<TextView>(R.id.tv_product_quantity)
        val tvNewQuantity = itemView.findViewById<TextView>(R.id.tv_product_new_quantity)
        val tvClient = itemView.findViewById<TextView>(R.id.tv_client)
        val tvDeltaValue = itemView.findViewById<TextView>(R.id.tv_delta_value)
        val ivProduct = itemView.findViewById<ImageView>(R.id.iv_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quantity = items[position].existingQuantity
        val increment = items[position].increment
        val type = items[position].type
        val title = items[position].productName
//        Log.e("items###","${items[position]}")

        holder.tvProductTitle.text = title
        holder.tvDate.text = "25.04.21(월)"
        holder.tvQuantity.text = quantity
        holder.tvNewQuantity.text = setNewQuantity(increment, type, quantity)
        holder.tvClient.text = "웅이상회"
        holder.tvDeltaValue.text = setTvDeltaValue(increment, type, holder.tvDeltaValue)
//        holder.tvDeltaValue.background = drawable.

        Glide.with(mContext)
            .load(items[position].productImage)
            .error(R.drawable.box)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(holder.ivProduct)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setTvDeltaValue(value: String, type: String, view: TextView): String {
        if (type.equals("in")){
            //입고
            view.background = ContextCompat.getDrawable(mContext, R.drawable.rounded_textview_type_in)
            view.setTextColor(ContextCompat.getColor(mContext, R.color.textview_type_in))
            return "+${value}"
        } else {
            //출고
            view.background = ContextCompat.getDrawable(mContext, R.drawable.rounded_textview)
            view.setTextColor(ContextCompat.getColor(mContext, R.color.textview_type_out))
            return "-${value}"
        }
    }

    fun setNewQuantity(value: String, type: String, quantity: String): String {
        val valueInt = value.toIntOrNull()
        val quantityInt = quantity.toIntOrNull()

        if (valueInt == null || quantityInt == null) {
            return "??"
        }

        return if (type.equals("in", ignoreCase = true)) {
            (quantityInt + valueInt).toString()
        } else {
            (quantityInt - valueInt).toString()
        }
    }

}
