package com.habahabamall.ShoppingCartApp.constants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.habahabamall.ShoppingCartApp.R

@Composable
fun AddWishAndCart() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    {
        ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.add_to_cart))
        }
        ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.add_to_wish))

        }
    }
}