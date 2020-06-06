package com.project.pocketdoc.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.DoctorType
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.glideImageInto
import com.project.pocketdoc.viewmodels.VisitViewModel
import com.project.pocketdoc.viewmodels.factories.VisitFactory
import kotlinx.android.synthetic.main.activity_visit.*
import kotlinx.android.synthetic.main.toolbar.view.*

class VisitActivity : AppCompatActivity() {

    private lateinit var viewModel: VisitViewModel

    private lateinit var visit: Visit
    private var cardId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)
        setSupportActionBar(toolbar_layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        toolbar_layout.toolbar_title.text = getString(R.string.view_visit)

        val visitId = intent.getIntExtra("visitId", 0)
        cardId = intent.getIntExtra("cardId", 0)
        if (visitId == 0) {
            Toast.makeText(this, getString(R.string.visit_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }
        if (cardId == 0) {
            Toast.makeText(this, getString(R.string.card_not_found), Toast.LENGTH_SHORT).show()
        }

        viewModel = ViewModelProvider(
            this,
            VisitFactory(
                VisitRepository(applicationContext),
                visitId,
                false
            )
        )
            .get(VisitViewModel::class.java)
        viewModel.visit.observe(this) {
            visit = it
            initViews()
        }

        pills_layout.setOnClickListener {
            val intent = Intent(this, PillsActivity::class.java)
            intent.putExtra("cardId", cardId)
            startActivity(intent)
        }

        card_layout.setOnClickListener {
            val intent = Intent(this, IllnessesActivity::class.java)
            intent.putExtra("cardId", cardId)
            intent.putExtra("visitId", visitId)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        tv_visit_id.text = getString(R.string.visit_id, visit.id)
        visit.price?.let { tv_visit_price.text = getString(R.string.tenge, it) }
        tv_doctor_role.text = DoctorType.getDoctorRole(visit.role)
        val doctor = visit.doctor
        if (doctor != null) {
            val doctorCard = doctor.profile?.card
            if (doctorCard != null) {
                tv_doctor_name.text = getString(R.string.name_format, doctorCard.firstName, doctorCard.lastName)
                tv_doctor_name.append("\n${doctor.clinic.name}")
                glideImageInto(this, imageDownloadUrl(doctorCard.id, doctorCard.fcmId), iv_profile_photo)
            } else
                tv_doctor_name.text = getString(R.string.doctor_not_active, doctor.id)
        }
    }
}
