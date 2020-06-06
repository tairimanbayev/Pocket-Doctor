package com.project.pocketdoc.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.OnCreateClick
import com.project.pocketdoc.adapters.PillDetailedAdapter
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.repo.PillRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.viewmodels.CreatePillsViewModel
import com.project.pocketdoc.viewmodels.factories.CreatePillsFactory
import kotlinx.android.synthetic.main.activity_create_pills.*
import kotlinx.android.synthetic.main.toolbar.view.*

class CreatePillsActivity : AppCompatActivity(), OnCreateClick {
    private val REQUEST_CODE = 262

    private lateinit var viewModel: CreatePillsViewModel
    private lateinit var card: Card
    private var visitId: Int = 0
    private val pillAdapter by lazy { PillDetailedAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pills)
        setSupportActionBar(toolbar_layout.findViewById(R.id.toolbar))
        supportActionBar?.let {
            title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_layout.toolbar_title.text = "Назначить лекарства"

        val cardId = intent.getIntExtra("cardId", 0)
        visitId = intent.getIntExtra("visitId", 0)
        if (cardId == 0 || visitId == 0) {
            Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            finish()
        }

        rv_pills.adapter = pillAdapter

        viewModel = ViewModelProvider(this,
            CreatePillsFactory(
                PillRepository(applicationContext), cardId, visitId
            )
        )
            .get(CreatePillsViewModel::class.java)

        viewModel.card.observe(this) {
            card = it
        }
        viewModel.status.observe(this) {
            when(it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> {
                    pillAdapter.items.clear()
                    pillAdapter.items.addAll(it.result)
                    pillAdapter.notifyDataSetChanged()
                }
            }
        }
        viewModel.illnessList.observe(this) {
            if (it is Status.Complete) {
                Log.d("CreatePillsActivLogcat", "onCreate: illnesse")
                pillAdapter.setIllnesses(it.result)
                pillAdapter.notifyDataSetChanged()
            }
        }
        viewModel.update.observe(this) {
            when(it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

        btn_add.setOnClickListener {
            pillAdapter.items.add(Pill())
            pillAdapter.notifyDataSetChanged()
        }

        btn_save.setOnClickListener { viewModel.updatePills(pillAdapter.items, pillAdapter.deletedItems) }
    }

    override fun onClick() {
        val intent = Intent(this, CardIllnessesActivity::class.java)
        intent.putExtra("cardId", card.id)
        intent.putExtra("visitId", visitId)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            viewModel.loadIllnesses()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
