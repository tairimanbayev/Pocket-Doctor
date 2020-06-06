package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_patient_card.view.*

class PatientCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profilePhoto: ImageView = itemView.iv_profile_photo
    var name: TextView = itemView.tv_profile_name
    var description: TextView = itemView.tv_description
    var number: TextView = itemView.tv_number
}