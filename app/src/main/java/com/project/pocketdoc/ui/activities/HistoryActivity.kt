package com.project.pocketdoc.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.HistoryAdapter
import com.project.pocketdoc.adapters.listeners.OnItemClickListener
import com.project.pocketdoc.adapters.listeners.OnLoadMoreListener
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.viewmodels.HistoryViewModel
import com.project.pocketdoc.viewmodels.factories.HistoryFactory
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toolbar.view.*

class HistoryActivity : AppCompatActivity(), OnItemClickListener<Visit>, OnLoadMoreListener {

    private val items = ArrayList<Visit>()
    private val historyAdapter = HistoryAdapter(items, this)
    private var pageId = 0
    private val cardId by lazy { intent.getIntExtra("cardId", 0) }
    private val viewModel by viewModels<HistoryViewModel> {
        HistoryFactory(VisitRepository(applicationContext), PageParams(pageId, 0, cardId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar_layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        toolbar_layout.toolbar_title.text = "История вызовов"

        if (cardId == 0) {
            Toast.makeText(this, getString(R.string.card_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        rv_visit.adapter = historyAdapter

        swipe_refresh_layout.setOnRefreshListener {
            pageId = 0
            viewModel.loadMore(pageId)
        }

        viewModel.status.observe(this) {
            if (it !is Status.Loading)
                swipe_refresh_layout.isRefreshing = false
            if (it is Status.Failure) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            else if (it is Status.Complete) {
                val visitPage = it.result
                for (v in visitPage.visits) {
                    if (!items.map { item -> item.id }.contains(v.id)) items.add(v)
                    else items[items.indexOfFirst { item -> item.id == v.id }] = v
                }

                if (visitPage.lastPage == false) {
                    pageId++
                    item_loading.isVisible = items.isEmpty()
                } else item_loading.hide()

                historyAdapter.setLoaded()
                historyAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(item: Visit) {
        val intent = Intent(this, VisitActivity::class.java)
        intent.putExtra("cardId", cardId)
        intent.putExtra("visitId", item.id)
        startActivity(intent)
    }

    override fun onLoadMore() {
        viewModel.loadMore(pageId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
