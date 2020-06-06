package com.project.pocketdoc.adapters.viewholders

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pill_detailed.view.*

class PillDetailedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var position: TextView = itemView.tv_position
    var remove: ImageView = itemView.btn_remove
    var name = itemView.et_name.editText!!
    var description = itemView.et_description.editText!!
    var createDiagnosis: TextView = itemView.tv_create_new_diagnosis
    var diagnosis: Spinner = itemView.sp_diagnosis
    var dose: EditText = itemView.et_dose
    var doseLbl: TextView = itemView.lbl_dose
    var doseSp: Spinner = itemView.sp_dose
    var date = itemView.et_date.editText!!
}