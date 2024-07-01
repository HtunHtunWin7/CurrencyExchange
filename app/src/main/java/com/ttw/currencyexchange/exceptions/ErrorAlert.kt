package com.ttw.currencyexchange.exceptions

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.ttw.currencyexchange.R

fun errorAlert(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
        .create()
    builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val view = LayoutInflater.from(context).inflate(R.layout.item_error_alert, null)
    val cancelButton = view.findViewById<MaterialButton>(R.id.btn_ok)
    val label = view.findViewById<TextView>(R.id.tv_error_message)
    label.text = message
    builder.setView(view)
    cancelButton.setOnClickListener {
        builder.dismiss()
    }
    builder.setCanceledOnTouchOutside(false)
    builder.show()
}