package com.example.arvex

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater

object LoadingInstance {

    lateinit var progressDialog: Dialog

    fun showLoading(context: Context) {
        progressDialog = Dialog(context).also {
            it.setContentView(
                LayoutInflater.from(context).inflate(R.layout.loading, null)
            )

            it.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            it.setCancelable(false)
            it.show()
        }
    }

    fun hideLoading() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}