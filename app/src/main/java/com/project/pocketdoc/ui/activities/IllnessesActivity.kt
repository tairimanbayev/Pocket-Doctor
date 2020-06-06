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
import com.project.pocketdoc.adapters.IllnessDetailedAdapter
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.repo.CardIllnessRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.hide
import com.project.pocketdoc.util.show
import com.project.pocketdoc.viewmodels.IllnessesViewModel
import com.project.pocketdoc.viewmodels.factories.CardIllnessesFactory
import kotlinx.android.synthetic.main.activity_illnesses.*
import kotlinx.android.synthetic.main.toolbar.view.*

class IllnessesActivity : AppCompatActivity() {

    private lateinit var viewModel: IllnessesViewModel
    private lateinit var adapter: IllnessDetailedAdapter
    private val items = ArrayList<Illness>()

    private var visitId = 0
    private var cardId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illnesses)
        setSupportActionBar(toolbar_layout.toolbar)
        toolbar_layout.toolbar_title.text = "Результаты вызова"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        visitId = intent.getIntExtra("visitId", 0)
        cardId = intent.getIntExtra("cardId", 0)
        viewModel = ViewModelProvider(
            this,
            CardIllnessesFactory(
                CardIllnessRepository(applicationContext), cardId, visitId, false
            )
        ).get(IllnessesViewModel::class.java)
        adapter = IllnessDetailedAdapter(this, items)
        rv_illnesses.adapter = adapter
        rv_illnesses.layoutManager = LinearLayoutManager(this)

        viewModel.status.observe(this) {
            progress_bar.isVisible = it is Status.Loading
            if (it is Status.Failure) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            else if (it is Status.Complete) {
                if (it.result.isEmpty()) {
                    rv_illnesses.hide()
                    tv_empty.show()
                } else {
                    rv_illnesses.show()
                    tv_empty.hide()
                    items.clear()
                    items.addAll(it.result)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
