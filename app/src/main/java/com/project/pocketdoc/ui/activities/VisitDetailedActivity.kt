package com.project.pocketdoc.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.adapters.HistoryCardPagedAdapter
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.jumpToActivity
import com.project.pocketdoc.util.reformat
import com.project.pocketdoc.viewmodels.VisitDetailedViewModel
import com.project.pocketdoc.viewmodels.factories.VisitFactory
import kotlinx.android.synthetic.main.activity_visit_detailed.*
import kotlinx.android.synthetic.main.toolbar.view.*

class VisitDetailedActivity : AppCompatActivity() {

    private lateinit var visit: Visit
    private val visitId by lazy { intent.getIntExtra("visitId", 0) }

    private val viewModel by viewModels<VisitDetailedViewModel> {
        VisitFactory(VisitRepository(applicationContext), visitId, true)
    }

    private val items = ArrayList<Card>()
    private val adapter by lazy { HistoryCardPagedAdapter(items, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_detailed)

        if (visitId == 0) {
            Toast.makeText(this, getString(R.string.visit_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        setSupportActionBar(toolbar_layout.toolbar)
        title = ""
        toolbar_layout.toolbar_title.text = getString(R.string.visit_id, visitId)

        view_pager.adapter = adapter

        viewModel.visit.observe(this) {
            visit = it
            initViews()
            items.clear()
            items.addAll(it.cards)
            adapter.notifyDataSetChanged()
        }

        lt_accept.setOnClickListener {
            viewModel.clicked(true)
        }
        lt_hide.setOnClickListener {
            viewModel.clicked(false)
        }

        viewModel.status.observe(this) {
            if (it is Status.Failure) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            else if (it is Status.Complete) {
                if (it.result) {
                    Toast.makeText(this, getString(R.string.visit_accepted), Toast.LENGTH_SHORT).show()
                    val extra = Bundle().apply { putInt("visitId", visitId) }
                    jumpToActivity(PatientDetailsActivity::class.java, extra)
                } else {
                    Toast.makeText(this, getString(R.string.visit_hidden), Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("visitId", visitId)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun initViews() {
        tv_reason.text = visit.reasons.fold("Причина: ") { output, i ->
            output + if (output.isNotEmpty()) ", ${i.reasonTitle}" else i.reasonTitle
        }
        tv_date.text = reformat(visit.date) ?: visit.date
        tv_card.text =
            if (visit.paymentCardId == null) getString(R.string.pay_with_cash) else getString(R.string.pay_with_card)
        tv_comment.text = if (visit.comment != null) visit.comment else getString(R.string.comment_not_set)
    }
}
