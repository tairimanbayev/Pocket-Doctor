package com.project.pocketdoc.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.IllnessDetailedViewHolder
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.services.FirebaseMsgService
import com.project.pocketdoc.util.glideImageInto

class IllnessDetailedAdapter(private val activity: Activity, private val items: ArrayList<Illness>) :
    RecyclerView.Adapter<IllnessDetailedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IllnessDetailedViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_illness_detailed, parent, false)
    )

    override fun getItemCount() = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IllnessDetailedViewHolder, position: Int) {
        val illness = items[position]
        holder.name.text = activity.resources.getString(R.string.name_format, illness.card.firstName, illness.card.lastName)
        holder.diagnosis.text = "Диагноз: ${illness.diagnosis}"
        holder.complaint.text = "Жалоба: ${illness.complaint}"
        holder.inspection.text = "Осмотр: ${illness.inspection}"
        holder.appointment.text = "Назначение: ${illness.appointment}"
        holder.result.text = "Результат: ${illness.result}"
        FirebaseMsgService.getToken(activity) {
            glideImageInto(activity, imageDownloadUrl(illness.card.id, it), holder.image)
        }
    }
}