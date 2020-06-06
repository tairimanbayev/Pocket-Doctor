package com.project.pocketdoc.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.IllnessAdapter
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.repo.CardIllnessRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.getText
import com.project.pocketdoc.util.setText
import com.project.pocketdoc.viewmodels.CardIllnessesViewModel
import com.project.pocketdoc.viewmodels.factories.CardIllnessesFactory
import kotlinx.android.synthetic.main.activity_card_illness.*
import kotlinx.android.synthetic.main.toolbar.view.*

class CardIllnessesActivity : AppCompatActivity() {

    private lateinit var illnessAdapter: IllnessAdapter
    private lateinit var card: Card
    private val visitId = intent.getIntExtra("visitId", -1)
    private val cardId = intent.getIntExtra("cardId", -1)
    private val viewModel by viewModels<CardIllnessesViewModel> {
        CardIllnessesFactory(CardIllnessRepository(applicationContext), cardId, visitId, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_illness)
        setSupportActionBar(toolbar_layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_layout.findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.card)

        illnessAdapter = IllnessAdapter(this)
        rv_illnesses.adapter = illnessAdapter

        if (visitId == -1 || cardId == -1) {
            Toast.makeText(this, getString(R.string.card_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.card.observe(this) {
            card = it
            et_chronic.setText(it.chronic ?: "")
            et_height.setText(it.height?.toString() ?: "")
            et_weight.setText(it.weight?.toString() ?: "")
        }

        viewModel.status.observe(this) {
            when (it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> {
                    illnessAdapter.items.clear()
                    illnessAdapter.items.addAll(it.result)
                    illnessAdapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.updateResult.observe(this) {
            when (it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> {
                    if (it.result) {
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }

        btn_save.setOnClickListener {
            viewModel.saveChanges(
                et_height.getText().toIntOrNull() ?: 0,
                et_weight.getText().toIntOrNull() ?: 0,
                et_chronic.getText(),
                illnessAdapter.items, illnessAdapter.deletedItems
            )
        }

        btn_add.setOnClickListener {
            illnessAdapter.items.add(Illness())
            illnessAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
