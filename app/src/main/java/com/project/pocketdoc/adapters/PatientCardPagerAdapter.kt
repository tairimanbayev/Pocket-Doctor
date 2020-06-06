package com.project.pocketdoc.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.PatientCardViewHolder
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.util.getDescription
import com.project.pocketdoc.util.glideImageInto

class PatientCardPagerAdapter(private val activity: Activity) : RecyclerView.Adapter<PatientCardViewHolder>() {

    private val TAG = "PatientCardPagerAdapterLogcat"
    val items = ArrayList<Card>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientCardViewHolder {
        return PatientCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_patient_card, parent, false))
    }

    override fun onBindViewHolder(holder: PatientCardViewHolder, position: Int) {
        val card = items[position]
        holder.apply {
            name.text = activity.resources.getString(R.string.name_format, card.firstName, card.lastName)
            description.text = getDescription(card.gender, card.birthday)
            number.text = card.profile?.phoneNumber.orEmpty()
            glideImageInto(activity, imageDownloadUrl(card.id, card.fcmId), profilePhoto)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(cards: List<Card>) {
        for (c in cards) {
            if (!items.map { it.id }.contains(c.id)) {
                items.add(c)
            } else {
                val index = items.indexOfFirst { it.id == c.id }
                if (items[index] != c)
                    items[index] = c
            }
        }
        notifyDataSetChanged()
    }
}