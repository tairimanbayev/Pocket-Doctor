package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pill.view.*

class PillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var pillName: TextView = itemView.tv_pill_name
}