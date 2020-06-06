package com.project.pocketdoc.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.project.pocketdoc.R

fun glideImageInto(activity: Activity, url: String, target: ImageView) {
    glideImage(activity, url).into(target)
}

fun glideImage(activity: Activity, url: String) =
    Glide.with(activity)
        .load(url).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
        .apply(RequestOptions.circleCropTransform()).centerCrop()

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun getText(field: TextInputLayout, onEmpty: () -> Unit): String {
    val text = field.getText()
    if (text.isEmpty()) {
        field.error = field.context.getString(R.string.empty_field)
        onEmpty()
    }
    return text
}

fun TextInputLayout.getText() = editText!!.text.toString()

fun TextInputLayout.setText(text: String) {
    editText!!.setText(text)
}

fun Activity.jumpToActivity(activity: Class<out Activity>, extras: Bundle = Bundle()) {
    startActivity(Intent(this, activity).apply { putExtras(extras) })
    finish()
}