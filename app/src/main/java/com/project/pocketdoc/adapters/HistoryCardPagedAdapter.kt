package com.project.pocketdoc.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.HistoryCardViewHolder
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.util.getDescription
import com.project.pocketdoc.util.glideImageInto

class HistoryCardPagedAdapter(private val items: List<Card>, private val activity: Activity) : RecyclerView.Adapter<HistoryCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCardViewHolder {
        return HistoryCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_history, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryCardViewHolder, position: Int) {
        val card = items[position]
        holder.name.text = activity.getString(R.string.name_format, card.firstName, card.lastName)
        holder.description.text = getDescription(card.gender, card.birthday)
        glideImageInto(activity, imageDownloadUrl(card.id, card.fcmId), holder.cardImage)
    }
}