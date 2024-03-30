package com.vd.study.core.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.vd.study.core.R
import com.vd.study.core.global.ThemeIdentifier

fun Context.showNetworkWarningDialog(themeIdentifier: ThemeIdentifier) {
    val dialog = Dialog(this)
    dialog.setContentView(
        if (themeIdentifier.isLightTheme) R.layout.network_warning_light_layout
        else R.layout.network_warning_dark_layout
    )

    val window = dialog.window
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val positiveButton = dialog.findViewById<Button>(R.id.dialog_button_positive)
    positiveButton.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}
