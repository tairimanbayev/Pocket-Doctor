package com.project.pocketdoc.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.google.android.material.tabs.TabLayoutMediator
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.PatientCardPagerAdapter
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.viewmodels.PatientDetailsViewModel
import com.project.pocketdoc.viewmodels.factories.PatientDetailsFactory
import kotlinx.android.synthetic.main.activity_patient_details.*
import kotlinx.android.synthetic.main.toolbar.view.*

class PatientDetailsActivity : AppCompatActivity() {

    private val REQUEST_CODE = 732

    private val visitId by lazy { intent.getIntExtra("visitId", -1) }
    private val viewModel by viewModels<PatientDetailsViewModel> {
        PatientDetailsFactory(VisitRepository(applicationContext), visitId)
    }
    private lateinit var visit: Visit
    private lateinit var adapter: PatientCardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_details)

        if (visitId == -1) {
            Toast.makeText(this, getString(R.string.visit_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        adapter = PatientCardPagerAdapter(this)
        vp_card.adapter = adapter
        TabLayoutMediator(tb_card, vp_card) { _, _ -> }.attach()
        setSupportActionBar(toolbar_layout.findViewById(R.id.toolbar))

        viewModel.visit.observe(this) {
            if (it.id == 0 || it.id == -1) {
                Toast.makeText(this, getString(R.string.visit_not_found), Toast.LENGTH_SHORT).show()
                finish()
            }
            visit = it
            adapter.updateList(visit.cards)
            btn_finish_visit.isVisible = visit.finishedAt == null
            tv_call_id.text = getString(R.string.visit_id, visit.id)
            visit.price?.let { tv_call_price.text = getString(R.string.tenge, it) }
            setActionBar()
        }

        viewModel.status.observe(this) {
            when (it) {
                is Status.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    btn_finish_visit.isEnabled = true
                }
                is Status.Complete -> Toast.makeText(this, getString(R.string.visit_finish_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        card_layout.setOnClickListener { startIntent(CardIllnessesActivity::class.java, forResult = true) }
        new_pills_layout.setOnClickListener { startIntent(CreatePillsActivity::class.java) }
        current_pills_layout.setOnClickListener { startIntent(PillsActivity::class.java, false) }
        history_layout.setOnClickListener { startIntent(HistoryActivity::class.java, false) }

        btn_finish_visit.setOnClickListener {
            it.isEnabled = false
            viewModel.finishVisit()
        }
    }

    private fun startIntent(activity: Class<out Activity>, withVisit: Boolean = true, forResult: Boolean = false) {
        val intent = Intent(this, activity)
        intent.putExtra("cardId", adapter.items[vp_card.currentItem].id)
        if (withVisit) intent.putExtra("visitId", visit.id)
        if (forResult) startActivityForResult(intent, REQUEST_CODE) else startActivity(intent)
    }

    private fun setActionBar() {
        supportActionBar?.apply {
            title = null
            toolbar_layout.toolbar_title.text = getString(
                if (visit.finishedAt != null) {
                    setDisplayHomeAsUpEnabled(true)
                    R.string.view_visit
                } else R.string.current_visit
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
            viewModel.getVisit()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        setResult(if (visit.finishedAt != null) RESULT_OK else RESULT_FIRST_USER)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
