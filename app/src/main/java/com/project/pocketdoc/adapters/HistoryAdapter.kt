package com.project.pocketdoc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.listeners.OnItemClickListener
import com.project.pocketdoc.adapters.listeners.OnLoadMoreListener
import com.project.pocketdoc.adapters.viewholders.HistoryViewHolder
import com.project.pocketdoc.model.tables.DoctorType
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.ui.activities.HistoryActivity
import com.project.pocketdoc.util.reformat

class HistoryAdapter(private val items: List<Visit>, activity: HistoryActivity) : RecyclerView.Adapter<HistoryViewHolder>() {

    private var isLoading = true
    private var onLoadMoreListener: OnLoadMoreListener = activity
    private var onItemClickListener: OnItemClickListener<Visit> = activity

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val itemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && itemCount <= (lastVisibleItemPosition + 5)) {
                    onLoadMoreListener.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val visit = items[position]
        holder.itemView.setOnClickListener { onItemClickListener.onClick(visit) }
        holder.reason.text = holder.itemView.context.resources.getString(R.string.reason,
            if (visit.reasons.isNotEmpty()) {
                var output = ""
                visit.reasons.forEach {
                    if (output.isNotEmpty()) output += ", "
                    output += it.reasonTitle
                }
                output
            } else "Не указана"
        )
        holder.doctorType.text = DoctorType.getDoctorRole(visit.role)
        holder.orderDate.text = reformat(visit.date) ?: visit.date
    }

    fun setLoaded() {
        isLoading = false
    }
}