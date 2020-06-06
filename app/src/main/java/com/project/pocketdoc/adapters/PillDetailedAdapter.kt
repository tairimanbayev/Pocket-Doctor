package com.project.pocketdoc.adapters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.viewholders.PillDetailedViewHolder
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.util.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PillDetailedAdapter(private val context: Context) : RecyclerView.Adapter<PillDetailedViewHolder>() {

    val items = ArrayList<Pill>()
    val deletedItems = ArrayList<Pill>()
    private val illnessString = ArrayList<String>()
    private val illnesses = ArrayList<Illness>()

    private val illnessAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, illnessString)
    private val periodAdapter =
        ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf("в день", "в неделю", "в месяц"))

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PillDetailedViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pill_detailed, parent, false)
    )

    override fun onBindViewHolder(holder: PillDetailedViewHolder, pos: Int) {
        val pill = items[pos]
        holder.apply {
            position.text = (pos + 1).toString()
            name.setText(pill.name)
            name.setTextChangeListener { pill.name = it }
            description.setText(pill.description)
            description.setTextChangeListener { pill.description = it }

            if (illnessString.size == 0) {
                createDiagnosis.setOnClickListener {
                    (context as OnCreateClick).onClick()
                }
                createDiagnosis.show()
                diagnosis.hide()
            } else {
                createDiagnosis.hide()
                diagnosis.show()
                diagnosis.adapter = illnessAdapter
                val illnessId = pill.illnessId
                diagnosis.setSelection(illnesses.indexOfFirst { it.id == illnessId })
                diagnosis.onItemSelectedListener = OnItemSelected {
                    pill.illnessId = illnesses[it].id
                }
            }

            val calendar = Calendar.getInstance()
            val endDate = pill.endDate
            if (endDate != null) {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                calendar.time = format.parse(endDate)
                date.setText(reformat(pill.endDate!!))
            }

            date.setOnClickListener {
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    pill.endDate = format.format(calendar.time)
                    date.setText(dateFormat.format(calendar.time))
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            val number = pill.getNotificationTime().size
            if (number != 0) {
                dose.setText("$number")
                doseLbl.text = context.resources.getQuantityText(R.plurals.times, number)
            }
            dose.setTextChangeListener {
                var n = if (it.isEmpty()) 0 else it.toInt()
                if (n > 7) {
                    n = 7
                    dose.setText("$n")
                }
                doseLbl.text = context.resources.getQuantityText(R.plurals.times, number)
                pill.setNotificationTime(getNotificationTimes(number, pill.period))
            }
            doseSp.adapter = periodAdapter
            when (pill.period) {
                7 -> doseSp.setSelection(1)
                30 -> doseSp.setSelection(2)
                else -> doseSp.setSelection(0)
            }
            doseSp.onItemSelectedListener = OnItemSelected {
                pill.period = when (it) {
                    1 -> 7
                    2 -> 30
                    else -> 1
                }
            }
            remove.setOnClickListener {
                itemView.clearFocus()
                onDelete(pos)
            }
        }
        illnessAdapter.notifyDataSetChanged()
    }

    fun setIllnesses(illnessList: List<Illness>) {
        illnessString.addAll(illnessList.map { it.diagnosis })
        illnesses.addAll(illnessList)
        illnessAdapter.notifyDataSetChanged()
    }

    private fun onDelete(position: Int) {
        AlertDialog.Builder(context).setMessage("Вы действительно хотите удалить данный диагноз?")
            .setPositiveButton("Да") { _, _ ->
                deletedItems.add(items[position])
                items.removeAt(position)
                notifyItemRemoved(position)
            }.setNegativeButton("Нет", null).create().show()
    }
}

interface OnCreateClick {
    fun onClick()
}

private class OnItemSelected(private val onSelected: (position: Int) -> Unit) : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelected(position)
    }
}