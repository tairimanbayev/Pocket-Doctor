package com.project.pocketdoc.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.DoctorType
import com.project.pocketdoc.model.tables.Profile
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.ui.activities.EditProfileActivity
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.getDescription
import com.project.pocketdoc.util.glideImageInto
import com.project.pocketdoc.viewmodels.ProfileViewModel
import com.project.pocketdoc.viewmodels.factories.ProfileFactory
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val TAG = "ProfileFragmentLogcat"

    private val viewModel by viewModels<ProfileViewModel> {
        ProfileFactory(
            ProfileRepository(requireContext().applicationContext),
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Failure -> Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> showProfile(it.result)
            }
        }
    }

    private fun showProfile(profile: Profile) {
        btn_edit.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("cardId", profile.card?.id ?: 0)
            startActivity(intent)
        }

        if (profile.card == null) {
            return
        }
        tv_profile_info.text = profile.doctor?.description
        tv_profile_name.text = String.format(getString(R.string.name_format), profile.card!!.firstName, profile.card!!.lastName)
        tv_doctor.text = DoctorType.getDoctorRole(profile.doctor?.role)
        tv_profile.text = getDescription(profile.card!!.gender, profile.card!!.birthday)
        glideImageInto(requireActivity(), imageDownloadUrl(profile.card!!.id, profile.fcmId), iv_profile_photo)
    }
}
