package com.project.pocketdoc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.PillViewHolder
import com.project.pocketdoc.model.tables.Pill

class PillAdapter(private val items: List<Pill>) : RecyclerView.Adapter<PillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillViewHolder {
        return PillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pill, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        holder.pillName.text = items[position].name
    }
}