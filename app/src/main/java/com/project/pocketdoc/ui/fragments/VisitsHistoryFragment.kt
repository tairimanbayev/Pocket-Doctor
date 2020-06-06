package com.project.pocketdoc.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.VisitAdapter
import com.project.pocketdoc.adapters.listeners.OnItemClickListener
import com.project.pocketdoc.adapters.listeners.OnLoadMoreListener
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.ui.activities.PatientDetailsActivity
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.viewmodels.VisitHistoryViewModel
import com.project.pocketdoc.viewmodels.factories.VisitListFactory
import kotlinx.android.synthetic.main.fragment_visits_history.*

class VisitsHistoryFragment : Fragment(), OnLoadMoreListener, OnItemClickListener<Visit> {
    private val TAG = "VisitsHistoryFragLogcat"

    private var pageNumber = 0
    private val items = ArrayList<Visit>()
    private val viewModel by viewModels<VisitHistoryViewModel> {
        VisitListFactory(VisitRepository(requireContext()), PageParams.getHistory(pageNumber))
    }
    private lateinit var patientAdapter: VisitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_visits_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientAdapter = VisitAdapter(items, this, VisitAdapter.VIEW_TYPE_PATIENT)

        rv_patients.adapter = patientAdapter

        swipe_refresh_layout.setOnRefreshListener {
            pageNumber = 0
            tv_error.hide()
            viewModel.loadMore(pageNumber)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it !is Status.Loading)
                swipe_refresh_layout.isRefreshing = false
            if (it is Status.Failure) {
                Log.d(TAG, "onViewCreated: ${it.message}")
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            } else if (it is Status.Complete) {
                val visitPage = it.result

                if (items.isNotEmpty() && items.last().id == -1) {
                    items.removeAt(items.lastIndex)
                    patientAdapter.notifyItemRemoved(items.lastIndex)
                }
                for (v in visitPage.visits) {
                    if (v.date.contains("2020")) {
                        if (!items.map { item -> item.id }.contains(v.id)) items.add(v)
                        else items[items.indexOfFirst { item -> item.id == v.id }] = v
                    }
                }
                if (visitPage.lastPage == false) {
                    tv_error.hide()
//                    items.add(Visit(id = -1))
                    pageNumber++
                } else
                    tv_error.isVisible = items.isEmpty()
                patientAdapter.setLoaded()
                patientAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onLoadMore() {
        viewModel.loadMore(pageNumber)
    }

    override fun onClick(item: Visit) {
        val intent = Intent(context, PatientDetailsActivity::class.java)
        intent.putExtra("visitId", item.id)
        startActivity(intent)
    }
}
