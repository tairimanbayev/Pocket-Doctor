package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_illness.view.*

class IllnessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var position: TextView = itemView.tv_position
    var diagnosis: TextInputEditText = itemView.et_diagnosis
    var complaint: TextInputEditText = itemView.et_complaint
    var appointment: TextInputEditText = itemView.et_appointment
    var inspection: TextInputEditText = itemView.et_inspection
    var result: TextInputEditText = itemView.et_result
    var remove: ImageView = itemView.btn_remove
}