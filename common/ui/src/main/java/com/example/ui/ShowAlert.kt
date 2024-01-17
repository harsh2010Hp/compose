package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

// Show  Error Alert
@Composable
fun ShowAlert(errorMessage: Int?, callback: () -> Unit) {
    SimpleAlertDialog(title = stringResource(R.string.error_title_text),
        message = stringResource(errorMessage ?: R.string.default_error),
        onDismiss = {
            callback()
        })
}
