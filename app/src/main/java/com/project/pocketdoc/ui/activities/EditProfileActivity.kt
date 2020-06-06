package com.project.pocketdoc.ui.activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.project.pocketdoc.R
import com.project.pocketdoc.imageDownloadUrl
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.util.Status
import com.project.pocketdoc.util.getText
import com.project.pocketdoc.util.glideImageInto
import com.project.pocketdoc.util.setText
import com.project.pocketdoc.viewmodels.EditProfileViewModel
import com.project.pocketdoc.viewmodels.factories.EditProfileFactory
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private val TAG = "EditProfileActivLogcat"
    private val GALLERY_CODE = 42
    private val CAMERA_CODE = 22
    private val STORAGE_REQUEST_CODE = 772

    private val cardId by lazy { intent.getIntExtra("cardId", 0) }
    private val viewModel by viewModels<EditProfileViewModel> {
        EditProfileFactory(ProfileRepository(applicationContext), cardId)
    }
    private lateinit var card: Card

    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(toolbar_layout.toolbar)
        toolbar_layout.toolbar_title.text = getString(R.string.edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.card.observe(this) {
            card = it
            initViews()
        }
        viewModel.status.observe(this) {
            if (it is Status.Failure) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            else if (it is Status.Complete) {
                if (it.result) {
                    Toast.makeText(this, getString(R.string.success_edit), Toast.LENGTH_SHORT).show()
                    finish()
                } else
                    Toast.makeText(this, getString(R.string.image_upload_failure), Toast.LENGTH_SHORT).show()
            }
        }

        et_birthday.editText!!.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val birthday = String.format("%02d.%02d.%04d", day, month + 1, year)
                et_birthday.setText(birthday)
                card.birthday = birthday
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btn_save.setOnClickListener { saveProfile() }
        iv_profile_photo.setOnClickListener { onImageClick() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                val uri = data?.data!!
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    updateImageView()
                } catch (e: FileNotFoundException) {
                    Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(this, "Не удалось загрузить фото", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == CAMERA_CODE) {
                bitmap = data?.extras?.get("data") as? Bitmap
                updateImageView()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE && grantResults.isEmpty())
            Toast.makeText(this, "Необходимо разрешение для доступа к локальным файлам", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        card.apply {
            et_last_name.setText(lastName)
            et_first_name.setText(firstName)
            et_middle_name.setText(middleName ?: "")
            et_birthday.setText(birthday)
            if (gender == 1) rb_male.toggle() else rb_female.toggle()
            et_height.setText(if (height == 0 || height == null) "" else height.toString())
            et_weight.setText(if (weight == 0 || weight == null) "" else weight.toString())
            glideImageInto(this@EditProfileActivity, imageDownloadUrl(id, fcmId), iv_profile_photo)
        }
    }

    private fun saveProfile() {
        var hasEmpty = false
        val setEmpty = { hasEmpty = true }
        card.lastName = getText(et_last_name, setEmpty)
        card.firstName = getText(et_first_name, setEmpty)
        card.middleName = et_middle_name.getText()
        card.gender = if (rb_male.isChecked) 1 else 0
        card.height = getText(et_height, setEmpty).toInt()
        card.weight = getText(et_weight, setEmpty).toInt()
        if (!hasEmpty) {
            viewModel.saveProfile(card, bitmap)
        }
    }

    private fun onImageClick() {
        AlertDialog.Builder(this).setTitle("Фотография")
            .setItems(arrayOf("Выбрать из галереи", "Сделать снимок")) { _, i ->
                when (i) {
                    0 -> {
                        pickPhoto()
                    }
                    1 -> {
                        takePhoto()
                    }
                }
            }.create().show()
    }

    private fun pickPhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_CODE)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CODE)
    }

    private fun updateImageView() {
        Log.d(TAG, "updateImageView: $bitmap")
        if (bitmap != null)
            Glide.with(this).load(bitmap).into(iv_profile_photo)
        else
            iv_profile_photo.setImageResource(R.drawable.ic_profile)
    }
}
