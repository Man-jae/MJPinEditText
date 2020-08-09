package com.example.mjpinedittext

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        et_code_1.removeTextChangedListener(textChangedListener)
        et_code_2.removeTextChangedListener(textChangedListener)
        et_code_3.removeTextChangedListener(textChangedListener)
        et_code_4.removeTextChangedListener(textChangedListener)
    }

    private fun setEditFocus(prev: EditText, next: EditText?) {
        next?.run {
            this.isEnabled = true
            this.requestFocus()
            prev.isEnabled = false
        } ?: also {
            prev.clearFocus()
            imm.hideSoftInputFromWindow(et_code_4.windowToken, 0)
        }
    }

    private fun setDelFocus(prev: EditText, next: EditText?) {
        next?.run {
            prev.isEnabled = true
            prev.requestFocus()
            prev.setText("")
            this.isEnabled = false
        } ?: also {
            et_code_4.setText("")
        }
    }

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrBlank()) {
                when {
                    et_code_1.isFocused -> setEditFocus(et_code_1, et_code_2)
                    et_code_2.isFocused -> setEditFocus(et_code_2, et_code_3)
                    et_code_3.isFocused -> setEditFocus(et_code_3, et_code_4)
                    et_code_4.isFocused -> setEditFocus(et_code_4, null)
                }
            }
        }
    }

    private val onKeyListener = View.OnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            when {
                et_code_2.isFocused -> setDelFocus(et_code_1, et_code_2)
                et_code_3.isFocused -> setDelFocus(et_code_2, et_code_3)
                et_code_4.isFocused -> setDelFocus(et_code_3, if (et_code_4.text.isNotEmpty()) null else et_code_4)
            }
        }
        return@OnKeyListener false
    }
}