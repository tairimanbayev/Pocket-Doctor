package com.project.pocketdoc.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.pocketdoc.R
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.ui.fragments.CurrentVisitFragment
import com.project.pocketdoc.ui.fragments.ProfileFragment
import com.project.pocketdoc.ui.fragments.VisitsHistoryFragment
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.viewmodels.VisitListViewModel
import com.project.pocketdoc.viewmodels.factories.VisitListFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mViewModel by viewModels<VisitListViewModel> {
        VisitListFactory(VisitRepository(applicationContext), PageParams.getActive(0))
    }

    private val REQUEST_CODE = 52

    private var profileFragment = ProfileFragment()
    private var currentVisitFragment = CurrentVisitFragment()
    private var visitsHistoryFragment = VisitsHistoryFragment()

    private lateinit var observer: Observer<Status<VisitPage>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observer = Observer<Status<VisitPage>> {
            when (it) {
                is Status.Complete -> {
                    if (it.result.visits.isNotEmpty()) {
                        val intent = Intent(this, PatientDetailsActivity::class.java)
                        intent.putExtra("visitId", it.result.visits[0].id)
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    initViews()
                }
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        mViewModel.status.observe(this, observer)
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            view_pager.currentItem = intent.getIntExtra("tab", 0)
        }
        super.onNewIntent(intent)
    }

    private fun initViews() {
        mViewModel.status.removeObserver(observer)
        view_pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int) = when (position) {
                0 -> currentVisitFragment
                1 -> visitsHistoryFragment
                2 -> profileFragment
                else -> throw IndexOutOfBoundsException("There are only 3 fragments")
            }
        }
        TabLayoutMediator(tab_layout, view_pager) { tab: TabLayout.Tab, i: Int ->
            tab.text = resources.getStringArray(R.array.tab_titles)[i]
        }.attach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_FIRST_USER) {
            finish()
        }
    }
}
