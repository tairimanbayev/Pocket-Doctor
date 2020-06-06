package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var doctorPhoto: ImageView = itemView.iv_doctor_photo
    var doctorType: TextView = itemView.tv_doctor_role
    var orderDate: TextView = itemView.tv_order_date
    var reason: TextView = itemView.tv_reason
}