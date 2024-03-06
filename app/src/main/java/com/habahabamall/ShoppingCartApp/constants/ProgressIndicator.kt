package com.habahabamall.ShoppingCartApp.constants

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.habahabamall.ShoppingCartApp.R

@Composable
fun ProgressIndicator(context: Context) {
    val checkConnection = isInternetAvailable(context)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        //there is no internet
        if (!checkConnection) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(Icons.Rounded.Warning, contentDescription = "offline")
                Text(text = stringResource(R.string.offline))

            }
        }
        else {

            CircularProgressIndicator(
                modifier = Modifier.size(200.dp),
                color = Color.Green,
                strokeWidth = 10.dp
            )
        }
    }
}