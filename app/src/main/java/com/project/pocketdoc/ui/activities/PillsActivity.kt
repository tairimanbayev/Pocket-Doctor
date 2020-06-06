package com.project.pocketdoc.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.PillAdapter
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.repo.PillRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.util.show
import com.project.pocketdoc.viewmodels.PillsFactory
import com.project.pocketdoc.viewmodels.PillsViewModel
import kotlinx.android.synthetic.main.activity_pills.*
import kotlinx.android.synthetic.main.toolbar.view.*

class PillsActivity : AppCompatActivity() {

    private lateinit var viewModel: PillsViewModel
    private val items = ArrayList<Pill>()
    private val adapter = PillAdapter(items)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills)
        setSupportActionBar(toolbar_layout.toolbar)
        title = ""
        toolbar_layout.toolbar_title.text = (getString(R.string.pills))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_pills.adapter = adapter
        rv_pills.layoutManager = LinearLayoutManager(this)
        val cardId = intent.getIntExtra("cardId", 0)
        viewModel =
            ViewModelProvider(this, PillsFactory(PillRepository(applicationContext), cardId)).get(PillsViewModel::class.java)

        viewModel.status.observe(this) {
            when (it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> {
                    items.clear()
                    if (it.result.isEmpty())
                        tv_no_pills.show()
                    else {
                        items.addAll(it.result)
                        tv_no_pills.hide()
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            progress_bar.isVisible = it is Status.Loading
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
