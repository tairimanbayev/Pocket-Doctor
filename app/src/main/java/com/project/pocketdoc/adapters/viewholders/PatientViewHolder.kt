package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_patient.view.*

class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.tv_name
    var description: TextView = itemView.tv_description
    var phoneNumber: TextView = itemView.tv_number
    var profilePhoto: ImageView = itemView.iv_profile_photo
}