package com.project.pocketdoc.ui.fragments

import android.app.Activity.RESULT_OK
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
import com.project.pocketdoc.ui.activities.VisitDetailedActivity
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.viewmodels.VisitCurrentViewModel
import com.project.pocketdoc.viewmodels.factories.VisitListFactory
import kotlinx.android.synthetic.main.fragment_current_visit.*

class CurrentVisitFragment : Fragment(), OnLoadMoreListener, OnItemClickListener<Visit> {
    private val TAG = "CurrentVisitFragLogcat"
    private val REQUEST_CODE = 283

    private var pageNumber = 0
    private val items = ArrayList<Visit>()
    private val viewModel by viewModels<VisitCurrentViewModel> {
        VisitListFactory(VisitRepository(requireContext()), PageParams.getZeros(pageNumber))
    }
    private lateinit var visitAdapter: VisitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_visit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visitAdapter = VisitAdapter(items, this, VisitAdapter.VIEW_TYPE_VISIT)

        rv_patients.adapter = visitAdapter

        swipe_refresh_layout.setOnRefreshListener {
            pageNumber = 0
            tv_error.hide()
            viewModel.loadMore(pageNumber)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it !is Status.Loading) swipe_refresh_layout.isRefreshing = false
            if (it is Status.Failure) {
                Log.d(TAG, "status observe: ${it.message}")
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            } else if (it is Status.Complete) {
                val visitPage = it.result
                if (items.isNotEmpty() && items.last().id == -1) {
                    items.removeAt(items.lastIndex)
                    visitAdapter.notifyItemRemoved(items.lastIndex)
                }
                for (v in visitPage.visits) {
                    if (!items.map { item -> item.id }.contains(v.id)) items.add(v)
                    else items[items.indexOfFirst { item -> item.id == v.id }] = v
                }

                if (visitPage.lastPage == false) {
                    tv_error.hide()
                    items.add(Visit(id = -1))
                    pageNumber++
                } else
                    tv_error.isVisible = items.isEmpty()
                visitAdapter.setLoaded()
                visitAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(item: Visit) {
        Log.d(TAG, "onClick: ${item.id}")
        val intent = Intent(context, VisitDetailedActivity::class.java)
        intent.putExtra("visitId", item.id)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onLoadMore() {
        viewModel.loadMore(pageNumber)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getIntExtra("visitId", -1)?.let { visitId ->
                items.forEach { if (it.id == visitId) items.remove(it) }
                visitAdapter.notifyDataSetChanged()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
