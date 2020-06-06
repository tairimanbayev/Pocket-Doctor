package com.project.pocketdoc.adapters

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextChangeListener(private val onTextChange:(s: String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChange(s.toString())
    }
}

fun EditText.setTextChangeListener(onTextChange: (s: String) -> Unit) {
    addTextChangedListener(TextChangeListener(onTextChange))
}