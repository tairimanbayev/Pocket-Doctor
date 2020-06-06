package com.project.pocketdoc.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.project.pocketdoc.R
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.jumpToActivity
import com.project.pocketdoc.viewmodels.LoginViewModel
import com.project.pocketdoc.viewmodels.factories.ProfileFactory
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> { ProfileFactory(ProfileRepository(applicationContext), true) }
    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setMask()

        btn_login.setOnClickListener {
            val password = et_password.text.toString()
            if (phoneNumber.isEmpty()) {
                et_phone_number.error = getString(R.string.missing_number)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                et_password.error = getString(R.string.missing_password)
                return@setOnClickListener
            }
            viewModel.login(phoneNumber, password)
        }

        viewModel.status.observe(this) {
            when (it) {
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Complete -> if (it.result) onLogin()
            }
        }
    }

    private fun setMask() {
        val listener = MaskedTextChangedListener(
            "+7 ([000]) [000] [00] [00]",
            et_phone_number,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                    phoneNumber = if (maskFilled) extractedValue else ""
                }
            })
        et_phone_number.apply {
            addTextChangedListener(listener)
            onFocusChangeListener = listener
            hint = listener.placeholder()
        }
    }

    private fun onLogin() {
        jumpToActivity(MainActivity::class.java)
    }
}
