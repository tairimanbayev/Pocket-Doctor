package com.project.pocketdoc.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.IllnessViewHolder
import com.project.pocketdoc.model.tables.Illness

class IllnessAdapter(private val context: Context) :
    RecyclerView.Adapter<IllnessViewHolder>() {

    val items = ArrayList<Illness>()
    val deletedItems = ArrayList<Illness>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IllnessViewHolder {
        return IllnessViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_illness, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IllnessViewHolder, pos: Int) {
        val illness = items[pos]
        holder.position.text = (pos + 1).toString()
        holder.diagnosis.setText(illness.diagnosis)
        holder.diagnosis.setTextChangeListener { illness.diagnosis = it }
        holder.appointment.setText(illness.appointment)
        holder.appointment.setTextChangeListener { illness.appointment = it }
        holder.complaint.setText(illness.complaint)
        holder.complaint.setTextChangeListener { illness.complaint = it }
        holder.inspection.setText(illness.inspection)
        holder.inspection.setTextChangeListener { illness.inspection = it }
        holder.result.setText(illness.result)
        holder.result.setTextChangeListener { illness.result = it }
        holder.remove.setOnClickListener { deleteItem(pos) }
    }

    private fun deleteItem(pos: Int) {
        AlertDialog.Builder(context).setMessage("Вы действительно хотите удалить диагноз?").setPositiveButton("Да") { _, _ ->
            deletedItems.add(items[pos])
            items.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, items.size)
        }.setNegativeButton("Нет", null).create().show()
    }
}