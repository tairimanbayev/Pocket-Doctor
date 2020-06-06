package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_illness_detailed.view.*

class IllnessDetailedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.iv_doctor_image
    var name: TextView = itemView.tv_doctor_name
    var diagnosis: TextView = itemView.tv_diagnosis
    var complaint: TextView = itemView.tv_complaint
    var inspection: TextView = itemView.tv_inspection
    var appointment: TextView = itemView.tv_appointment
    var result: TextView = itemView.tv_result
}