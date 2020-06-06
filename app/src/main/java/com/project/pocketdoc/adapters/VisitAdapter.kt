package com.project.pocketdoc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.listeners.OnItemClickListener
import com.project.pocketdoc.adapters.listeners.OnLoadMoreListener
import com.project.pocketdoc.adapters.viewholders.LoadingViewHolder
import com.project.pocketdoc.adapters.viewholders.PatientViewHolder
import com.project.pocketdoc.adapters.viewholders.VisitViewHolder
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.util.getDescription
import com.project.pocketdoc.util.glideImage
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.util.show

class VisitAdapter(private val items: List<Visit>, private val fragment: Fragment, private val viewType: Int) :
    RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val VIEW_TYPE_VISIT = 0
        const val VIEW_TYPE_PATIENT = 1
        private const val VIEW_TYPE_LOADING = 2
    }

    private var isLoading = true

    private val onLoadMoreListener = fragment as? OnLoadMoreListener
    private val onItemClickListener = fragment as? OnItemClickListener<Visit>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val itemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && itemCount <= (lastVisibleItemPosition + 5)) {
                    onLoadMoreListener?.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_VISIT -> VisitViewHolder(inflater.inflate(R.layout.item_visit, parent, false))
            VIEW_TYPE_PATIENT -> PatientViewHolder(inflater.inflate(R.layout.item_patient, parent, false))
            else -> LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_LOADING) {
            val visit = items[position]
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClick(visit)
            }
            val cards = visit.cards
            if (cards.isNotEmpty()) {
                val card = cards[0]
                val fullname = fragment.resources.getString(R.string.name_format, card.firstName, card.lastName)
                val description = getDescription(card.gender, card.birthday)
                val builder = glideImage(fragment.requireActivity(), imageDownloadUrl(card.id, card.fcmId))
                if (holder is VisitViewHolder) {
                    if (cards.size > 1) {
                        holder.users.text =
                            fragment.resources.getQuantityString(R.plurals.patients_number, cards.size, cards.size)
                        holder.icons.show()
                    } else {
                        holder.users.text = ""
                        holder.icons.hide()
                    }
                    val reasons = visit.reasons
                    holder.reason.text = fragment.resources.getString(
                        R.string.reason,
                        if (reasons.isNotEmpty()) {
                            var output = ""
                            for (r in reasons) {
                                if (output.isNotEmpty()) output += ", "
                                output += r.reasonTitle
                            }
                            output
                        } else "не указана"
                    )
                    holder.name.text = fullname
                    holder.description.text = description
                    builder.into(holder.profilePhoto)
                } else if (holder is PatientViewHolder) {
                    holder.phoneNumber.text = card.profile?.phoneNumber.orEmpty()
                    holder.name.text = fullname
                    holder.description.text = description
                    builder.into(holder.profilePhoto)
                }
            }
        }
    }

    fun setLoaded() {
        isLoading = false
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        if (position == items.size - 1 && items[position].id == -1) VIEW_TYPE_LOADING else viewType
}