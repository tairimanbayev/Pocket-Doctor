package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card_history.view.*

class HistoryCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cardImage: ImageView = itemView.iv_card_image
    var name: TextView = itemView.tv_card_name
    var description: TextView = itemView.tv_card_description
}