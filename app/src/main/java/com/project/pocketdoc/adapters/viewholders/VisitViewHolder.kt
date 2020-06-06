package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_visit.view.*

class VisitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.tv_name
    var description: TextView = itemView.tv_description
    var reason: TextView = itemView.tv_reason
    var profilePhoto: ImageView = itemView.iv_profile_photo
    var icons: ImageView = itemView.iv_profile_icons
    var users: TextView = itemView.tv_users
}